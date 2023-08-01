import os
import pandas as pd
from matplotlib import pyplot as plt
import random
import networkx as nx
import argparse


def string_to_int(reference_id):
    try:
        return int(reference_id)
    except ValueError:
        return reference_id




def reactions_for_central_node(reaction_type, central_node, all_reactions):
    reactions = all_reactions[(all_reactions['reference_id'] == central_node) & (all_reactions['reference_type'] == reaction_type)]

    return reactions




def reactions_for_node_by_date(date, reaction_type, central_node, all_reactions):
    reactions = reactions_for_central_node(reaction_type, central_node, all_reactions)
    reactions_by_date = reactions[reactions['created_at'].dt.date == pd.Timestamp(date).date()]

    return reactions_by_date




def plot_network(date, reaction_type, reactions_labels, central_node, all_reactions, folder_path):

    if reaction_type != 'retweeted':
        print(f'Started building graph for {reactions_labels[reaction_type]} posted on {date}...')
        # print(f'Started filtering the reactions...')
        reactions_by_date = reactions_for_node_by_date(date, reaction_type, central_node, all_reactions)
        # print(f'Reactions dataframe ready. Starting building the graph...')

        # Create an empty directed graph
        G = nx.DiGraph()

        # Add the source tweet as the central node
        G.add_node(central_node)

        # Add the reply tweets as nodes and edges
        reaction_nodes = reactions_by_date['tweet_id'].to_numpy()
        G.add_nodes_from(reaction_nodes)
        G.add_edges_from(zip(reaction_nodes, [central_node] * len(reaction_nodes)))
        # print(f'Added all {G.number_of_nodes()} nodes. Now grouping the reactions by author_id...')

        # group the original dataframe by 'author_id' and count the number of occurrences
        grouped = reactions_by_date.groupby('author_id').size()

        # keep only the groups where the count is greater than 1
        grouped = grouped[grouped > 1]
        # print('Grouped reactions where author posted more than 1 reaction.')

        # create a new dataframe with the desired columns and 'author_count'
        df_authors_with_multiple_reactions = reactions_by_date[['tweet_id', 'author_id']].loc[reactions_by_date['author_id'].isin(grouped.index)]
        df_authors_with_multiple_reactions['author_count'] = df_authors_with_multiple_reactions['author_id'].apply(lambda x: grouped[x])
        # print('Created helper dataframe.')

        unique_author_counts = df_authors_with_multiple_reactions['author_count'].unique()
        color_dict = {count: "#" + ''.join([random.choice('0123456789ABCDEF') for j in range(6)])
                    for count in unique_author_counts}
        # print('Created node color dictionary.')


        def is_author_with_multiple_reactions(node, df_authors_with_multiple_reactions):
            return node in df_authors_with_multiple_reactions['tweet_id'].values

        def get_author_count(node, df_authors_with_multiple_reactions):
            row = df_authors_with_multiple_reactions.loc[df_authors_with_multiple_reactions['tweet_id'] == node]
            author_count = row['author_count'].iloc[0]

            return author_count

        # print('Adding colors to the node_colors list...')
        node_colors = []
        for node in G.nodes():
            if node == central_node:
                node_colors.append('red')
            else:
                node_colors.append(color_dict[get_author_count(node, df_authors_with_multiple_reactions)] 
                                if is_author_with_multiple_reactions(node, df_authors_with_multiple_reactions) else 'black')
        # print('Added all colors.')

        # Set the node size to 20
        node_size = 20
        # Set the edge color to grey and the opacity to 0.7
        edge_color = 'grey'
        edge_alpha = 0.7
        # print('Creating the layout...')
        # get the spring layout
        pos = nx.spring_layout(G)

        # print('Started drawing the graph...')
        # Draw the graph
        nx.draw(G, pos=pos, with_labels=False, node_size=node_size, node_color=node_colors, edge_color=edge_color, alpha=edge_alpha)

        # print('Finished drawing. Now saving to file...')
        path = os.path.join(folder_path, f'{reactions_labels[reaction_type]}_network_{date}.png')
        plt.savefig(path)
        plt.close()
    else:
        print(f'Appending retweets info on {date} to the summary file...')
        reactions_by_date = reactions_for_node_by_date(date, reaction_type, central_node, all_reactions)
        path = os.path.join(folder_path, f'{reactions_labels[reaction_type]}_summary.txt')
        new_line = f'On {date}, there were {len(reactions_by_date)} retweets.'

        try:
            with open(path, 'x') as f:
                f.write(new_line + '\n')
        except FileExistsError:
            # Open the file in read mode
            with open(path, 'r') as file:
                # Read all the lines in the file and store them in a list
                lines = file.readlines()

            # Open the file in append mode
            with open(path, 'a') as file:
                # If the line is not already in the file, write it to the file
                if new_line + '\n' not in lines:
                    file.write(new_line + '\n')



    print('Done')





def plot_all_networks(reaction_types_list, reactions_labels, dates_list, central_node, all_reactions, root_path):
    for reaction_type in reaction_types_list:
        folder_path = os.path.join(root_path, f'{reactions_labels[reaction_type]}')
        # Check if the folder exists
        if not os.path.exists(folder_path):
            # If the folder doesn't exist, create it
            os.makedirs(folder_path)
        for date in dates_list:
            plot_network(date, reaction_type, reactions_labels, central_node, all_reactions, folder_path)



def create_graphs_subfolders(graphs_dir_path, reactions_labels):
    for label in reactions_labels.values():
        subfolder = os.path.join(graphs_dir_path, label)
        # Check if the folder exists
        if not os.path.exists(subfolder):
            # If the folder doesn't exist, create it
            os.makedirs(subfolder)


def main():

    networkx_dir_path = os.path.dirname(os.path.abspath(__file__))
    output_dir_path = os.path.join(networkx_dir_path, 'graphs')

    # Check if the folder exists
    if not os.path.exists(output_dir_path):
        # If the folder doesn't exist, create it
        os.makedirs(output_dir_path)

    # Due to the fact that some machines may have limited processing resources,
    # there is no need to regenerate the data files from the full merged dataset.
    # It already exists on the remote GitHub repository associated with this project at:
    # https://github.com/andreistoica12/twitter-provenance/tree/dev/src/main/python/networkx/graphs

    reactions_labels = {
        'quoted': 'quotes',
        'replied_to': 'replies',
        'retweeted': 'retweets'
    }

    create_graphs_subfolders(output_dir_path, reactions_labels)

    # Get a list of subfolders in the main folder
    subfolders = [f.path for f in os.scandir(output_dir_path) if f.is_dir()]

    print("Module 'networkx':")

    counter = 0
    # Check if each subfolder contains files
    for subfolder in subfolders:
        files_in_subfolder = os.listdir(subfolder)
        if len(files_in_subfolder) > 0:
            print(f"Graphs for the {os.path.split(subfolder)[-1]} have already been generated.")
            counter+=1
            
    if counter == 3:
        print("All data has already been generated.")
        return

    python_dir_path = os.path.dirname(networkx_dir_path)
    # input_filename = 'covaxxy_merged_test.csv'
    input_filename = 'covaxxy_merged_25_days.csv'
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

    print("Filtering the reactions...")
    # Filter the dataset and keep only the reactions. Remove source tweets.
    all_reactions = merged_days[merged_days['reference_id'] != '#']
    print("Retrieving the tweet with the most reactions...")
    central_node = all_reactions.loc[merged_days['tweet_id'].notnull()]['reference_id'].value_counts().idxmax()

    reaction_types_list = ['quoted', 'replied_to', 'retweeted']

    dates_list = ['2021-03-01', '2021-03-02','2021-03-03', '2021-03-04', '2021-03-05']

    print("Building graphs...")
    plot_all_networks(reaction_types_list, reactions_labels, dates_list, central_node, all_reactions, output_dir_path)
    print("All graphs generated.")

    # # In order to avoid boilerplate code, I decided to add some command line arguments when running
    # # this script, instead of creating a separte, almost identical, script file for each date or modifying in the code.
    # # The argument is: --date .
    # # This way, if the user wishes to run the script in a terminal window, he/she can specify this
    # # argument themselves. The steps to parse the command line argument are the following:
    # # 1. Create an argument parser
    # parser = argparse.ArgumentParser()

    # # 2. Add argument for: date
    # parser.add_argument('--date', type=str, default='march_1', help='Date to analyze tweets from')

    # # 3. Parse the command-line arguments
    # args = parser.parse_args()

    # date_key = args.date

    # dates = {
    #     'march_1': '2021-03-01'
    # }

    # time_intervals = {
    #     "night": "MIDNIGHT - 9AM",
    #     "working_hours": "9AM - 5PM",
    #     "evening": "5PM - MIDNIGHT"
    # }

    # if date_key not in dates:
    #     raise Exception("Invalid --date argument value provided or date inexistent. Format should be month_day (e.g. march_1).")
        
    # date = dates[date_key]
    
    # model3_dir_path = os.path.dirname(os.path.abspath(__file__))
    # outputdir_path = os.path.join(model3_dir_path, 'data', date_key)

    # # Get a list of all files and directories in the output folder, if any already
    # day_data_folder_contents = os.listdir(outputdir_path)

    # # Due to the fact that some machines may have limited processing resources,
    # # there is no need to regenerate the data files from the full merged dataset.
    # # It already exists on the remote GitHub repository associated with this project at:
    # # https://github.com/andreistoica12/twitter-provenance/tree/dev/src/main/python/model3/data

    # # Check if there are files other than ".gitkeep" in the folder
    # if len(day_data_folder_contents) > 1 or (len(day_data_folder_contents) == 1 and ".gitkeep" not in day_data_folder_contents):
    #     date_object = datetime.strptime(date, '%Y-%m-%d')
    #     formatted_date = date_object.strftime('%B %d, %Y')
    #     print(f"Model3: Data for {formatted_date} has already been generated.")
    #     return


    # python_dir_path = os.path.dirname(model3_dir_path)
    # input_filename = f'dataset_{date_key}.csv'
    # input_data_path = os.path.join(python_dir_path, 'input-data', 'model3', input_filename)


    # print(f"Reading input dataset ({input_filename})...")
    # dataset = pd.read_csv(input_data_path)
    # print(f"Input dataset read. Number of tweets: {len(dataset)}.")

    # print("Preprocessing the input dataframe...")
    # top_rows = preprocess_dataset_for_model3(dataset, date)

    # output_dirpath = os.path.join(model3_dir_path, 'data', date_key)
    
    # print("Creating the output JSON file...")
    # create_json_files_one_day(top_rows, dates, date, time_intervals, output_dirpath)

    # print("Output file created successfully.")

    



if __name__ == "__main__":
    main()
