package com.rug;

import org.python.util.PythonInterpreter;


public class PythonRunner {
    public static void main(String[] args) {
        executePythonCode();
    }

    public static void executePythonCode() {
        try (PythonInterpreter pyInterpreter = new PythonInterpreter()) {
            pyInterpreter.exec("from py_test import MyPythonClass");
    
            // Create an instance of the Python class
            pyInterpreter.exec("my_class = MyPythonClass()");
            
            // Call methods on the Python class
            pyInterpreter.exec("my_class.save_result_to_json(result, 'output.json')");        }
    }
}
