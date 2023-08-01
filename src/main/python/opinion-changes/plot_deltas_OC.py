import os
import json
from matplotlib import pyplot as plt
from collections import Counter
import textwrap
from matplotlib import pyplot as plt
import argparse





def create_path_to_opinion_changes(reaction_types, opinion_changes_dir_path):
    """Function to create the path to the opinion changes JSON file, based on the reaction types we took into consideration.

    Args:
        reaction_types (list): list of reaction types

    Returns:
        str: path to the opinion changes file
    """    
    type = "_".join(reaction_types)
    path = os.path.join(opinion_changes_dir_path, f'{type}_OC.json')

    return path





def load_opinion_changes(path_to_opinion_changes):
    """Function that generates a dictionary based on a JSON file which contains the opinion changes within the reactions of the dataset.

    Args:
        path_to_opinion_changes (str): path to the JSON file associated with the opinion changes within the reactions
                                               (e.g. /your/path/to/research-internship/files/opinion-changes-25_days/quoted_OC.json)

    Returns:
        dict: the original dictionary containing opinion changes from reactions
    """    
    with open(path_to_opinion_changes) as f:
        # Load the JSON data into a Python dictionary
        opinion_changes_from_file = json.load(f)
        # Create a new dictionary with tuple keys
        original_opinion_changes = {}
        for key in opinion_changes_from_file:
            # Convert the string key to a tuple
            new_key = eval(key)
            # Add the key-value pair to the new dictionary
            original_opinion_changes[new_key] = opinion_changes_from_file[key]
            
    return original_opinion_changes





def compute_biggest_opinion_changes_deltas(path_to_opinion_changes):
    opinion_changes = load_opinion_changes(path_to_opinion_changes)
    deltas = { key: max(value) - min(value) for key, value in opinion_changes.items() }

    return deltas





# function to add value labels - adds the value of y
def add_labels_y_value(x,y):
    """Function that takes the x and y-axis to be passed onto a plot function and generates labels,
    such that on top of each y value, it is displayed centrally.

    Args:
        x (list): list of labels for x-axis of a plot
        y (list): list of values for y-axis of a plot
    """    
    for i in range(len(x)):
        plt.text(x[i], y[i], y[i], ha = 'center', va = 'bottom')





def plot_deltas_OC(reaction_types, deltas_labels, reactions_labels, root_path, number_of_days, opinion_changes_dir_path):
    opinion_changes_deltas = compute_biggest_opinion_changes_deltas(create_path_to_opinion_changes(reaction_types, opinion_changes_dir_path))
    # Get the values from the dictionary
    deltas = list(opinion_changes_deltas.values())

    # Use Counter to count the occurrences of each value
    deltas_count = Counter(deltas)
    percentages = { f'{deltas_labels[pair[0]]} ({pair[0]})': round(pair[1] / sum(deltas_count.values()) * 100, 1) 
               for pair in sorted(deltas_count.most_common(), key=lambda x: x[0])}
    
    keys = list(percentages.keys())
    values = list(percentages.values())

    # Create a bar chart of the counts
    plt.bar(keys, values, edgecolor='black')
    plt.xticks(rotation=45)
    plt.subplots_adjust(bottom=0.25)
    # Add labels to the top of each bar
    add_labels_y_value(keys, values)
    plt.xlabel('Intensity of opinion changes')
    plt.ylabel('Percentage of opinion changes')

    long_title = f'Distribution of Opinion Changes: Percentage by Intensity for { ", ".join(map(lambda x: reactions_labels[x], reaction_types)) }'
    # Wrap the title onto multiple lines
    wrapped_title = textwrap.fill(long_title, width=50)
    plt.title(wrapped_title, loc="center", pad=10)

    types = "_".join(reaction_types)
    path = os.path.join(root_path, f'{types}_deltas_OC_{number_of_days}.png')
    plt.savefig(path)
    plt.close()




def plot_all_deltas_OC(reaction_types_full_list, deltas_labels, reactions_labels, root_path, number_of_days, opinion_changes_dir_path):
    for reaction_types in reaction_types_full_list:
        plot_deltas_OC(reaction_types, deltas_labels, reactions_labels, root_path, number_of_days, opinion_changes_dir_path)




def main():

    current_dir_path = os.path.dirname(os.path.abspath(__file__))
    python_dir_path = os.path.dirname(current_dir_path)

    dataset_possibilities = ['15_days', '25_days']
    # number_of_days = dataset_possibilities[0]
    number_of_days = dataset_possibilities[1]

    input_dir_path = os.path.join(python_dir_path, 'input-data', f'opinion-changes-{number_of_days}')
    graphs_dir_path = os.path.join(current_dir_path, 'graphs')
    output_dir_path = os.path.join(graphs_dir_path, f'deltas-OC-{number_of_days}')

    output_dir_contents = os.listdir(output_dir_path)

    # Check if there are files other than ".gitkeep" in the folder
    if len(output_dir_contents) > 1 or (len(output_dir_contents) == 1 and ".gitkeep" not in output_dir_contents):
        print(f"Module 'opinion changes': Data for the {number_of_days} dataset has already been generated.")
        return

    reaction_types_full_list = [
                                ['quoted'], 
                                # ['quoted', 'retweeted'], 
                                ['replied_to'], 
                                ['replied_to', 'quoted'], 
                                # ['replied_to', 'quoted', 'retweeted'],
                                # ['replied_to', 'retweeted']
                               ]
    

    reactions_labels = {
        'quoted': 'quotes',
        'replied_to': 'replies',
        'retweeted': 'retweets'
    }

    deltas_labels = {
        2: 'minimum',
        3: 'slight',
        4: 'considerable',
        5: 'big',
        6: 'very big',
        7: 'huge',
        8: 'maximum'
    }


    plot_all_deltas_OC(reaction_types_full_list, deltas_labels, reactions_labels, output_dir_path, number_of_days, input_dir_path)


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
