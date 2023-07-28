import os
import json
import pandas as pd
from datetime import datetime



def string_to_int(reference_id):
    try:
        return int(reference_id)
    except ValueError:
        return reference_id



def get_key_from_value_in_dict(target_value, dict):
    # Use dictionary comprehension to find the key with the target value
    target_key = next((key for key, value in dict.items() if value == target_value), None)
    return target_key




def timestamp_offset_aware_to_naive(timestamp):
    # Check if the data type of the timestamp is datetime.datetime
    if not isinstance(timestamp, datetime):
        raise ValueError("Input 'timestamp' must be of type datetime.datetime")
    
    offset_naive = timestamp.replace(tzinfo=None)

    return offset_naive




def preprocess_dataset_for_model3(dataset, date):
    dataset.set_index('index', inplace=True)
    # Convert the 'created_at' column to datetime, handling '#' characters as None
    dataset['created_at'] = pd.to_datetime(dataset['created_at'], errors='coerce')
    dataset = dataset[dataset['created_at'].notna()]
    dataset['created_at'] = dataset['created_at'].apply(timestamp_offset_aware_to_naive)
    # Here, after conversion to local timestamps, some values were converted to other dates as well.
    # Due to the unknown nature of the timestamps in the initial dataset, I could not predict how many tweets
    # were actually posted on another date on the local timezone, so I ran the conversion algorithm on a fixed
    # number of UTC tweets. However, now I need to make them timezone-naive and remove the ones with local date
    # different than the date we are looking for.
    # Filter the DataFrame to keep only rows where the date is the chosen date
    dataset = dataset[dataset['created_at'].dt.date == pd.to_datetime(date).date()]

    return dataset




def create_datetime_object(date, time):
    # Convert date string to a date object
    date_object = datetime.strptime(date, '%Y-%m-%d').date()
    
    # Convert time string to a time object
    time_object = datetime.strptime(time, '%I%p').time()

    # Combine date and time objects to create a datetime object
    datetime_object = datetime.combine(date_object, time_object)

    return datetime_object




def create_json_one_interval(dataset, date, interval):
    # Parse the date and interval strings which come as input parameters into datetime objects
    # Step 1: Parse the time interval string to extract the start time and end time
    interval_boundaries = interval.split('-')
    start_time = interval_boundaries[0].strip()
    end_time = interval_boundaries[1].strip()

    # Step 2: Combine the date and start/end times into datetime objects 
    if (start_time != 'MIDNIGHT' and end_time != 'MIDNIGHT'):
        start_datetime = create_datetime_object(date, start_time)
        end_datetime = create_datetime_object(date, end_time)
        # Retrieve the rows in the dataset where we have textual tweets posted in the specified time interval on the given day
        dataset_for_interval = dataset[(dataset['created_at'] >= start_datetime) & (dataset['created_at'] < end_datetime)]
    elif start_time == 'MIDNIGHT':
        # Retrieve the rows in the dataset where we have textual tweets posted in the "night" interval.
        # Because there is no suggestive way defining the midnight hour, i.e. the first hour of the day recorded,
        # (it is 12AM, but for most people this notation is not the most straightforward), I eliminated the first filter condition
        end_datetime = create_datetime_object(date, end_time)
        dataset_for_interval = dataset[(dataset['created_at'] < end_datetime)]
    elif end_time == 'MIDNIGHT':
        # Retrieve the rows in the dataset where we have textual tweets posted in the "evening" interval.
        # Because there is no constant defining the midnight hour, i.e. the last ending boundary of the day,
        # I eliminated the second filter condition
        start_datetime = create_datetime_object(date, start_time)
        dataset_for_interval = dataset[(dataset['created_at'] >= start_datetime)]

    json_object = {}

    # Convert the date string to the desired format 'March_01' for the id's
    date_object = datetime.strptime(date, '%Y-%m-%d')
    formatted_date = date_object.strftime('%B_%d')
    json_object['all_textual_tweets_at_timepoint_id'] = f"all_textual_tweets_on_{formatted_date}_{start_time}_{end_time}"

    # Format the datetime object as "March 1, 2021"
    formatted_date_for_date_key = date_object.strftime('%B %d, %Y')
    json_object['date'] = formatted_date_for_date_key

    json_object["time_interval"] = interval
    json_object["percentage_out_of_day_tweets"] = f"{round(len(dataset_for_interval) / len(dataset) * 100, 2)}%"
    json_object["nr_of_original_tweets"] = len(dataset_for_interval[dataset_for_interval['reference_id'] == '#'])
    json_object["nr_of_replies"] = len(dataset_for_interval[dataset_for_interval['reference_type'] == "replied_to"])
    json_object["nr_of_quotes"] = len(dataset_for_interval[dataset_for_interval['reference_type'] == "quoted"])
    json_object["post_or_react_id"] = f"post_or_react_on_{formatted_date}_{start_time}_{end_time}"
    json_object["group_of_authors_id"] = f"group_of_authors_on_{formatted_date}_{start_time}_{end_time}"
    json_object["nr_of_distinct_authors"] = dataset_for_interval['author_id'].nunique()

    # Retrieving the 10 rows with the largest values in the 'followers_count' column - top 10 influencers
    top_10_influencers = dataset_for_interval.nlargest(10, 'followers_count')
    # Calculate the average of the values in the 'followers_count' column
    average_followers_count_top_10_influencers = round(top_10_influencers['followers_count'].mean())
    json_object["avg_nr_of_followers_top_10_influencers"] = "{:,}".format(average_followers_count_top_10_influencers)

    average_followers_count_all_users = round(dataset_for_interval['followers_count'].mean())
    json_object["avg_nr_of_followers_all_users"] = "{:,}".format(average_followers_count_all_users)


    # Convert all values to strings
    for key in json_object:
        json_object[key] = str(json_object[key])

    return json_object  




def create_json_files_one_day(day_dataset, dates, date, time_intervals, dirpath):
    for interval_key, interval_value in time_intervals.items():
        data = create_json_one_interval(day_dataset, date, interval_value)
        # Write the dictionary to a JSON file
        path = os.path.join(dirpath, f"data3_{get_key_from_value_in_dict(date, dates)}_{interval_key}.json")
        with open(path, "w") as json_file:
            json.dump(data, json_file, indent=4)





def main():

    dates = {
        "march_1": '2021-03-01'
    }

    time_intervals = {
        "night": "MIDNIGHT - 9AM",
        "working_hours": "9AM - 5PM",
        "evening": "5PM - MIDNIGHT"
    }

    date = dates['march_1']
    date_key = get_key_from_value_in_dict(date, dates)
    
    model3_dir_path = os.path.dirname(os.path.abspath(__file__))
    outputdir_path = os.path.join(model3_dir_path, 'data', date_key)

    # Get a list of all files and directories in the output folder, if any already
    day_data_folder_contents = os.listdir(outputdir_path)

    # Due to the fact that some machines may have limited processing resources,
    # there is no need to regenerate the data files from the full merged dataset.
    # It already exists on the remote GitHub repository associated with this project at:
    # https://github.com/andreistoica12/twitter-provenance/tree/dev/src/main/python/model3/data

    # Check if there are files other than ".gitkeep" in the folder
    if len(day_data_folder_contents) > 1 or (len(day_data_folder_contents) == 1 and ".gitkeep" not in day_data_folder_contents):
        date_object = datetime.strptime(date, '%Y-%m-%d')
        formatted_date = date_object.strftime('%B %d, %Y')
        print(f"Model3: Data for {formatted_date} has already been generated.")
        return


    python_dir_path = os.path.dirname(model3_dir_path)
    input_filename = f'dataset_{date_key}.csv'
    input_data_path = os.path.join(python_dir_path, 'input-data', 'model3', input_filename)


    print(f"Reading input dataset ({input_filename})...")
    dataset = pd.read_csv(input_data_path)
    print(f"Input dataset read. Number of tweets: {len(dataset)}.")

    print("Preprocessing the input dataframe...")
    top_rows = preprocess_dataset_for_model3(dataset, date)

    output_dirpath = os.path.join(model3_dir_path, 'data', date_key)
    
    print("Creating the output JSON file...")
    create_json_files_one_day(top_rows, dates, date, time_intervals, output_dirpath)

    print("Output file created successfully.")

    



if __name__ == "__main__":
    main()
