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
    python_dir_path = os.path.dirname(model1_dir_path)
    input_filename = 'covaxxy_merged_test.csv'
    input_data_path = os.path.join(python_dir_path, 'input-data', input_filename)

    print("Reading input data...")
    merged_days = pd.read_csv(input_data_path)
    print("Input data read.")

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

    output_path = os.path.join(model1_dir_path, 'data', 'data1.json')

    # Write the dictionary to a JSON file
    with open(output_path, "w") as output_json_file:
        json.dump(data, output_json_file, indent=4)

    print("Output file created successfully.")

    

    # # In order to avoid boilerplate code, I decided to add some command line arguments when running
    # # this script, instead of creating a separte, almost identical, file for each (combination of) reaction types.
    # # The arguments are: --input, --output, --reactions_index .
    # # This way, if the user wishes to run the script in a terminal window, he/she can specify these
    # # arguments themselves. The steps to parse the command line arguments are the following:
    # # 1. Create an argument parser
    # parser = argparse.ArgumentParser()

    # # 2. Add arguments for: input CSV file, output folder and the reactions_index variable
    # parser.add_argument('--input', type=str, help='Input CSV file path')
    # parser.add_argument('--output', type=str, help='Output folder path')
    # parser.add_argument('--reactions_index', type=int, default=2, help='The type of reactions we consider')

    # # 3. Parse the command-line arguments
    # args = parser.parse_args()

    # data_path = args.input
    # opinion_changes_path = args.output
    # # Instead of creating all files at once, I decided to create them one at a time, due to memory restrictions.
    # # More specifically, the univeristy cluster's resource scheduler needs me to specify the memory is should allocate
    # # beforehand. Because the script initially created all files at once, I needed to reserve a lot of memory,
    # # so the SLURM job would be scheduled too late. This was not necessary, because for each file, most operations
    # # need to be performed again, there is no global state variable which can be reused. 
    # # This is why I opted for the following option.
    # reaction_types = reaction_types_full_list[args.reactions_index]
    # print(f'Reactions considered: {reaction_types}')
    



if __name__ == "__main__":
    main()
