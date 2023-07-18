import json

class MyPythonClass:
    def __init__(self, param1):
        self.param1 = param1


    def save_result_to_json(self, file_path):
        # Assume you have a Python object or dictionary that you want to save as JSON
        data = {
            "name": "John",
            "age": 30,
            "city": "New York"
        }

        # Open the file in write mode
        with open(file_path, "w") as json_file:
            json.dump(data, json_file)

        print("JSON file saved successfully.")