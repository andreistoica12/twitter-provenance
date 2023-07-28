import os
import pandas as pd
from datetime import datetime
import argparse
from timezonefinder import TimezoneFinder
from geopy.geocoders import Nominatim
import pytz



def string_to_int(reference_id):
    try:
        return int(reference_id)
    except ValueError:
        return reference_id





def get_timezone_from_location(location_name):
    geolocator = Nominatim(user_agent="timezone_finder", timeout=100)
    location = geolocator.geocode(location_name)

    if location is None:
        return None  # Unable to find location

    # Get latitude and longitude from the location
    latitude, longitude = location.latitude, location.longitude

    # Use timezonefinder to get the timezone from latitude and longitude
    timezone_finder = TimezoneFinder()
    timezone_str = timezone_finder.timezone_at(lng=longitude, lat=latitude)

    if timezone_str is not None:
        timezone = pytz.timezone(timezone_str)
        return timezone
    else:
        return None




def tweet_convert_utc_to_local_timezone(tweet_id, dataset):
    utc_time = dataset.loc[dataset["tweet_id"] == tweet_id, 'created_at'].item()
    location_name = dataset.loc[dataset["tweet_id"] == tweet_id, 'location'].item()

    try:
        timezone = get_timezone_from_location(location_name)
        return utc_time.astimezone(timezone) if timezone != None else '#'
    except Exception as e:
        print(f"Error occurred for tweet_id={tweet_id}: {e}")
        return '#'




def df_convert_utc_to_local_timezone(dataset):
    local_timestamps = []
    for tweet_id in dataset['tweet_id']:
        local_timestamps.append(tweet_convert_utc_to_local_timezone(tweet_id, dataset))

    dataset['created_at'] = local_timestamps




def get_key_from_value_in_dict(target_value, dict):
    # Use dictionary comprehension to find the key with the target value
    target_key = next((key for key, value in dict.items() if value == target_value), None)
    return target_key




def main():


    # In order to avoid boilerplate code, I decided to add some command line arguments when running
    # this script, instead of creating a separte, almost identical, script file for each date or modifying in the code.
    # The argument is: --date .
    # This way, if the user wishes to run the script in a terminal window, he/she can specify this
    # argument themselves. The steps to parse the command line argument are the following:
    # 1. Create an argument parser
    parser = argparse.ArgumentParser()

    # 2. Add argument for: date
    parser.add_argument('--date', type=str, default='march_1', help='Date to analyze tweets from')

    # 3. Parse the command-line arguments
    args = parser.parse_args()

    date_key = args.date

    dates = {
        "march_1": '2021-03-01'
    }


    if date_key not in dates:
        raise Exception("Invalid --date argument value provided or date inexistent. Format should be month_day (e.g. march_1).")
        
    date = dates[date_key]
    


    model3_dir_path = os.path.dirname(os.path.abspath(__file__))
    python_dir_path = os.path.dirname(model3_dir_path)

    outputdir_path = os.path.join(python_dir_path, 'input-data', 'model3')
    output_filename = f"dataset_{date_key}.csv"
    output_file_path = os.path.join(outputdir_path, output_filename)

    # Due to the fact that some machines may have limited processing resources,
    # there is no need to regenerate the data files from the full merged dataset.
    # It already exists on the remote GitHub repository associated with this project at:
    # https://github.com/andreistoica12/twitter-provenance/tree/dev/src/main/python/input-data/model3

    # Check if the file already exists
    if os.path.exists(output_file_path):
        date_object = datetime.strptime(date, '%Y-%m-%d')
        formatted_date = date_object.strftime('%B %d, %Y')
        print(f"Model3: Preliminary dataset for {formatted_date} has already been generated.")
        return

    input_filename = 'covaxxy_merged_test.csv'
    # input_filename = 'covaxxy_merged_25_days.csv'
    input_data_path = os.path.join(python_dir_path, 'input-data', input_filename)


    print(f"Reading input data ({input_filename})...")
    merged_days = pd.read_csv(input_data_path)
    print(f"Input data read. Number of tweets: {len(merged_days)}.")

    print("Applying transformations to input dataframe...")
    merged_days.drop_duplicates(subset=['tweet_id'], inplace=True)
    merged_days.reset_index(drop=True, inplace=True)
    merged_days['reference_id'] = merged_days['reference_id'].apply(string_to_int)
    # Convert the 'created_at' column to datetime
    merged_days['created_at'] = pd.to_datetime(merged_days['created_at'])


    print("Filtering dataframe...")
    day_data = merged_days[merged_days['created_at'].dt.date == pd.to_datetime(date).date()]

    # Filter the dataset and keep only the tweets with a value for location 
    # (although this doesn't mean the location itself is valid and parseable).
    tweets_with_location = day_data[day_data['location'] != '#']

    tweets_with_location = tweets_with_location[tweets_with_location['reference_type'] != 'retweeted']

    number_of_top_tweets = 10000

    # Get the top 10k rows with the largest 'like_count' values
    top_rows = tweets_with_location.nlargest(number_of_top_tweets, 'like_count')

    df_convert_utc_to_local_timezone(top_rows)
    top_rows.index.name = 'index'

    print("Creating the output CSV file...")
    top_rows.to_csv(output_file_path)
    print(f"Output file {output_filename} created successfully.")

    



if __name__ == "__main__":
    main()
