import os
import json
import pandas as pd


reaction_labels = {
    'quoted': 'QUOTE',
    'replied_to': 'REPLY',
    'retweeted': 'RETWEET'
}



def string_to_int(reference_id):
    try:
        return int(reference_id)
    except ValueError:
        return reference_id



def create_json_original(original_tweet_id, dataset):

    has_duplicates = dataset[dataset['tweet_id'] == original_tweet_id]['tweet_id'].duplicated().any()
    if has_duplicates:
        raise Exception("Duplicate tweet id. Make sure the tweet id is unique.")
    
    def row_value_for(column_name):
        return dataset.loc[dataset['tweet_id'] == original_tweet_id, column_name].item()

    original = {}
    original["original_tweet_id"] = original_tweet_id
    unfiltered_text = row_value_for('text')
    original["original_text"] = unfiltered_text.replace('\n', '')
    original["ORIGINAL_created_at"] = row_value_for('created_at')
    original["ORIGINAL_location"] = row_value_for('location')
    original["post_id"] = f"post_{original_tweet_id}"
    original["original_author_id"] = f"ORIGINAL_TWEET_author_{row_value_for('author_id')}"
    original["ag_o_name"] = row_value_for('name')
    original["original_author_props_id"] = f"original_author_props_{row_value_for('author_id')}"
    original["ORIGINAL_credible"] = row_value_for('credible')
    original["ORIGINAL_username"] = row_value_for('username')
    original["ORIGINAL_verified"] = row_value_for('verified')
    original["ORIGINAL_followers_count"] = row_value_for('followers_count')
    original["ORIGINAL_following_count"] = row_value_for('following_count')

    # Convert all values to strings
    for key in original:
        original[key] = str(original[key])

    return original



def row_value_for(column_name, original_tweet_id, dataset):
    return dataset.loc[dataset['tweet_id'] == original_tweet_id, column_name].item()



def parse_offset_string(offset_string):
    if offset_string.endswith("min"):  # Offset in minutes
        offset_minutes = int(offset_string[:-3])  # Extract the numeric part of the string (excluding the last 3 characters)
        return pd.Timedelta(minutes=offset_minutes)
    elif offset_string.endswith("h"):  # Offset in hours
        offset_hours = int(offset_string[:-1])  # Extract the numeric part of the string (excluding the last character)
        return pd.Timedelta(hours=offset_hours)
    else:
        raise ValueError("Invalid offset string. It should end with 'min' for minutes or h for hours.")
    


def get_reactions_to_original_tweet_in_interval(original_tweet_id, start_of_interval, end_of_interval, dataset):
    
    original_posting_time = row_value_for('created_at', original_tweet_id, dataset)

    offset1 = parse_offset_string(start_of_interval)
    start_time = original_posting_time + offset1

    if end_of_interval == "LAST_REACTION":
        end_time = pd.Timestamp.utcnow()
    else:
        offset2 = parse_offset_string(end_of_interval)
        end_time = original_posting_time + offset2

    reactions = dataset[(dataset['reference_id'] == original_tweet_id) &
                        (dataset['reference_type'] != '#') &
                        (dataset['created_at'] >= start_time) & 
                        (dataset['created_at'] < end_time)]

    return reactions



def create_json_group_of_reactions(original_tweet_id, start_of_interval, end_of_interval, dataset, total_nr_of_reactions):
    has_duplicates = dataset[dataset['tweet_id'] == original_tweet_id]['tweet_id'].duplicated().any()
    if has_duplicates:
        raise Exception("Duplicate tweet id. Make sure the tweet id is unique.")
    
    reactions = get_reactions_to_original_tweet_in_interval(original_tweet_id, start_of_interval, end_of_interval, dataset)
    nr_of_unique_author_ids = reactions['author_id'].nunique()

    
    group_of_reactions = {}
    group_of_reactions["react_id"] = f"reacts_for_{start_of_interval}_{end_of_interval}"
    group_of_reactions["reaction_group_of_authors_id"] = f"reactions_authors_for_{start_of_interval}_{end_of_interval}"
    group_of_reactions["nr_of_distinct_authors"] = nr_of_unique_author_ids
    group_of_reactions["reaction_group_of_tweets_id"] = f"reactions_for_{start_of_interval}_{end_of_interval}"
    group_of_reactions["time_interval"] = f"{start_of_interval} - {end_of_interval}"
    group_of_reactions["nr_of_reactions"] = len(reactions)
    group_of_reactions["percentage_out_of_total_reactions"] = f"{round(len(reactions) / total_nr_of_reactions * 100, 2)}%"
    group_of_reactions["nr_of_replies"] = reactions['reference_type'].value_counts().get('replied_to', 0)
    group_of_reactions["nr_of_quotes"] = reactions['reference_type'].value_counts().get('quoted', 0)
    group_of_reactions["nr_of_retweets"] = reactions['reference_type'].value_counts().get('retweeted', 0)

    # Convert all values to strings
    for key in group_of_reactions:
        group_of_reactions[key] = str(group_of_reactions[key])

    return group_of_reactions



def create_json_data(original_tweet_id, reaction_interval, dataset, total_nr_of_reactions):
    # Split the string based on the "-"
    intervals_boundaries = reaction_interval.split("-")
    start_of_interval = intervals_boundaries[0]
    end_of_interval = intervals_boundaries[1]

    data = {}
    data["original"] = create_json_original(original_tweet_id, dataset)
    data["group_of_reactions"] = create_json_group_of_reactions(original_tweet_id, start_of_interval, end_of_interval, dataset, total_nr_of_reactions)

    return data



def create_all_json_data(original_tweet_id, reaction_intervals, dataset, dirpath):
    if not isinstance(reaction_intervals, dict):
        raise TypeError("The reaction intervals have to be written in a dictionary.")
    
    total_nr_of_reactions = len(dataset[(dataset['reference_id'] == original_tweet_id) &
                               (dataset['reference_type'] != '#')])
    
    for interval in reaction_intervals.values():
        data = create_json_data(original_tweet_id, interval, dataset, total_nr_of_reactions)

        path = os.path.join(dirpath, f"data2_{interval}.json")

        # Write the dictionary to a JSON file
        with open(path, "w") as json_file:
            json.dump(data, json_file, indent=4)





def main():
    
    model2_dir_path = os.path.dirname(os.path.abspath(__file__))
    outputdir_path = os.path.join(model2_dir_path, 'data')

    # Get a list of all files and directories in the output folder, if any already
    data_folder_contents = os.listdir(outputdir_path)

    # Due to the fact that some machines may have limited processing resources,
    # there is no need to regenerate the data files from the full merged dataset.
    # It already exists on the remote GitHub repository associated with this project at:
    # https://github.com/andreistoica12/twitter-provenance/tree/dev/src/main/python/model2/data

    # Check if there are files other than ".gitkeep" in the folder
    if len(data_folder_contents) > 1 or (len(data_folder_contents) == 1 and ".gitkeep" not in data_folder_contents):
        print("Model2: Data has already been generated.")
        return

    python_dir_path = os.path.dirname(model2_dir_path)
    # input_filename = 'covaxxy_merged_test.csv'
    input_filename = 'covaxxy_merged_25_days.csv'
    input_data_path = os.path.join(python_dir_path, 'input-data', input_filename)


    print(f"Reading input data ({input_filename})...")
    merged_days = pd.read_csv(input_data_path)
    print(f"Input data read. Number of tweets: {len(merged_days)}.")

    print("Applying transformations to input dataframe...")
    merged_days['reference_id'] = merged_days['reference_id'].apply(string_to_int)
    # Convert the 'created_at' column to datetime
    merged_days['created_at'] = pd.to_datetime(merged_days['created_at'])

    print("Filtering the reactions...")
    # Filter the dataset and keep only the reactions. Remove source tweets.
    all_reactions = merged_days[merged_days['reference_id'] != '#']
    # I make sure the tweet with the most reaction exists in our dataset.
    print("Retrieving the tweet with the most reactions...")
    tweet_id_most_reactions = all_reactions.loc[merged_days['tweet_id'].notnull()]['reference_id'].value_counts().idxmax()

    reaction_intervals = {
        0: "0h-15min",
        1: "15min-1h",
        2: "1h-2h",
        3: "2h-3h",
        4: "3h-6h",
        5: "6h-12h",
        6: "12h-24h",
        7: "24h-48h",
        8: "48h-72h",
        9: "72h-LAST_REACTION"
    }

    print("Creating the output JSON files...")
    output_dirpath = os.path.join(model2_dir_path, 'data')

    create_all_json_data(tweet_id_most_reactions, reaction_intervals, merged_days, output_dirpath)

    print("Output files created successfully.")

    



if __name__ == "__main__":
    main()
