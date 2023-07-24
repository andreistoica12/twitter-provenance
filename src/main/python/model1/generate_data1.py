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
    original["post_id"] = f"post_{original_tweet_id}"
    original["original_author_id"] = f"author_ORIGINAL_TWEET_{row_value_for('author_id')}"
    original["ag_o_name"] = row_value_for('name')
    original["author_props_id"] = f"author_props_{row_value_for('author_id')}"
    original["credible"] = row_value_for('credible')
    original["username"] = row_value_for('username')
    original["verified"] = row_value_for('verified')
    original["followers_count"] = row_value_for('followers_count')
    original["following_count"] = row_value_for('following_count')
    original["original_tweet_id"] = f"tweet_{original_tweet_id}"
    unfiltered_text = row_value_for('text')
    original["original_text"] = unfiltered_text.replace('\n', '')
    original["original_tweet_props_id"] = f"tweet_props_{original_tweet_id}"
    original["ORIGINAL_properties"] = "ORIGINAL TWEET properties"
    original["ORIGINAL_created_at"] = row_value_for('created_at')
    original["ORIGINAL_location"] = row_value_for('location')
    original["ORIGINAL_like_count"] = row_value_for('like_count')
    original["ORIGINAL_quote_count"] = row_value_for('quote_count')
    original["ORIGINAL_reply_count"] = row_value_for('reply_count')
    original["ORIGINAL_retweet_count"] = row_value_for('retweet_count')

    # Convert all values to strings
    for key in original:
        original[key] = str(original[key])

    return original


def create_json_reaction(reaction_tweet_id, dataset):
    has_duplicates = dataset[dataset['tweet_id'] == reaction_tweet_id]['tweet_id'].duplicated().any()
    if has_duplicates:
        raise Exception("Duplicate tweet id. Make sure the tweet id is unique.")
    
    def row_value_for(column_name):
        return dataset.loc[dataset['tweet_id'] == reaction_tweet_id, column_name].item()
    
    reaction_type = reaction_labels[f"{row_value_for('reference_type')}"]

    reaction = {}
    reaction["react_id"] = f"react_{reaction_type}_{reaction_tweet_id}"
    reaction["reaction_author_id"] = f"author_{reaction_type}_{row_value_for('author_id')}"
    reaction["ag_r_name"] = row_value_for('name')
    reaction["author_props_id"] = f"author_props_{row_value_for('author_id')}"
    reaction["credible"] = row_value_for('credible')
    reaction["username"] = row_value_for('username')
    reaction["verified"] = row_value_for('verified')
    reaction["followers_count"] = row_value_for('followers_count')
    reaction["following_count"] = row_value_for('following_count')
    reaction["reaction_tweet_id"] = f"tweet_{reaction_tweet_id}"
    reaction["reply_retweet_quote"] = reaction_type
    unfiltered_text = row_value_for('text')
    reaction["reaction_text"] = unfiltered_text.replace('\n', '')
    reaction["reaction_tweet_props_id"] = f"{reaction_type}_props_{reaction_tweet_id}"
    reaction["REACTION_properties"] = f"{reaction_type} properties"
    reaction["REACTION_created_at"] = row_value_for('created_at')
    reaction["REACTION_location"] = row_value_for('location')
    reaction["REACTION_like_count"] = row_value_for('like_count')
    reaction["REACTION_retweet_count"] = row_value_for('retweet_count')
    reaction["REACTION_reference_id"] = row_value_for('reference_id')

    # Convert all values to strings
    for key in reaction:
        reaction[key] = str(reaction[key])

    return reaction


def create_json_reaction_list(original_tweet_id, dataset):
    # retrieve unique types of reactions. Reactions have a value assigned to the 'reference_type' field, different than '#'
    # ('#' represent original tweets)
    tweet_types = dataset['reference_type'].unique()
    reaction_types = tweet_types[tweet_types != '#']

    reactions_of_original_tweet = dataset[(dataset['reference_type'].isin(reaction_types)) & (dataset['reference_id'] == original_tweet_id)]

    top_reactions = []
    for reaction_type in reaction_types:
        # I will choose the most liked reaction for each type of reaction: 
        # reply, quote and retweet, as they generate the most interest.
        # Due to the fact that in my dataset, retweets have no likes, as well as no quotes and replies, 
        # I chose to select the number of retweets as the factor for choosing the top retweet.
        factor = 'like_count'
        factor = 'retweet_count' if reaction_type == 'retweeted' else factor

        idx_top_reaction = reactions_of_original_tweet[reactions_of_original_tweet['reference_type'] == reaction_type][factor].idxmax()
        top_reaction_tweet_id = reactions_of_original_tweet.loc[idx_top_reaction, 'tweet_id']

        json_top_reaction = create_json_reaction(top_reaction_tweet_id, reactions_of_original_tweet)

        top_reactions.append(json_top_reaction)


    return top_reactions
    

def create_json_data(original_tweet_id, dataset):
    data = {}
    data["original"] = create_json_original(original_tweet_id, dataset)
    data["reactions"] = create_json_reaction_list(original_tweet_id, dataset)

    return data





def main():
    
    model1_dir_path = os.path.dirname(os.path.abspath(__file__))
    output_dirpath = os.path.join(model1_dir_path, 'data')

    # Get a list of all files and directories in the output folder, if any already
    data_folder_contents = os.listdir(output_dirpath)

    # Due to the fact that some machines may have limited processing resources,
    # there is no need to regenerate the data files from the full merged dataset.
    # It already exists on the remote GitHub repository associated with this project at:
    # https://github.com/andreistoica12/twitter-provenance/tree/dev/src/main/python/model1/data

    # Check if there are files other than ".gitkeep" in the folder
    if len(data_folder_contents) > 1 or (len(data_folder_contents) == 1 and ".gitkeep" not in data_folder_contents):
        print("Model1: Data has already been generated.")
        return


    python_dir_path = os.path.dirname(model1_dir_path)
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

    print("Creating the output JSON file...")
    data = create_json_data(tweet_id_most_reactions, merged_days)

    output_path = os.path.join(output_dirpath, "data1.json")

    # Write the dictionary to a JSON file
    with open(output_path, "w") as output_json_file:
        json.dump(data, output_json_file, indent=4)

    print("Output file created successfully.")




if __name__ == "__main__":
    main()
