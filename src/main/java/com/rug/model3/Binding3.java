package com.rug.model3;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.interop.InteropFramework;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;







public class Binding3 {

    private final Template3 template;
    private final DataClass3 data;
    
    public Binding3(Template3 template, DataClass3 data) {
        this.template = template;
        this.data = data;
    }


    public void addAllTextualTweetsAtTimepoint(String all_textual_tweets_at_timepoint_id, String date, 
                                 String time_interval, String percentage_out_of_day_tweets, String nr_of_original_tweets,
                                 String nr_of_replies, String nr_of_quotes,
                                 Bindings bindings) {
        bindings.addVariable(template.qn_var("all_textual_tweets_at_timepoint_id"), template.qn_tw(all_textual_tweets_at_timepoint_id));
        bindings.addAttribute("date", date);
        bindings.addAttribute("time_interval", time_interval);
        bindings.addAttribute("percentage_out_of_day_tweets", percentage_out_of_day_tweets);
        bindings.addAttribute("nr_of_original_tweets", nr_of_original_tweets);
        bindings.addAttribute("nr_of_replies", nr_of_replies);
        bindings.addAttribute("nr_of_quotes", nr_of_quotes);
        // bindings.addAttribute("nr_of_retweets", nr_of_retweets);
    }


    public void addPostOrReactActivity(String post_or_react_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("post_or_react_id"), template.qn_tw(post_or_react_id));        
    }


    public void addGroupOfAuthors(String group_of_authors_id, String nr_of_distinct_authors, 
                                  String avg_nr_of_followers_top_10_influencers,
                                  String avg_nr_of_followers_all_users,
                                  Bindings bindings) {
        bindings.addVariable(template.qn_var("group_of_authors_id"), template.qn_tw(group_of_authors_id));
        bindings.addAttribute("nr_of_distinct_authors", nr_of_distinct_authors);
        bindings.addAttribute("avg_nr_of_followers_top_10_influencers", avg_nr_of_followers_top_10_influencers);
        bindings.addAttribute("avg_nr_of_followers_all_users", avg_nr_of_followers_all_users);
    }

    

    public void addJsonObject(Bindings bindings) {
        addAllTextualTweetsAtTimepoint(data.getAllTextualTweetsAtTimepointId(), data.getDate(),
                                       data.getTimeInterval(), data.getPercentageOutOfDayTweets(), data.getNrOfOriginalTweets(),
                                       data.getNrOfReplies(), data.getNrOfQuotes(),
                                       bindings);
        addPostOrReactActivity(data.getPostOrReactId(), bindings);
        addGroupOfAuthors(data.getGroupOfAuthorsId(), data.getNrOfDistinctAuthors(),
                          data.getAvgNrOfFollowersTop10Influencers(), data.getAvgNrOfFollowersAllUsers(),
                          bindings);
    }





    public void bind(String filePathString) {
        ProvFactory pFactory = template.getpFactory();

        Bindings bindings = new Bindings(pFactory);

        addJsonObject(bindings);


        bindings.addVariableBindingsAsAttributeBindings();
        bindings.exportToJson(filePathString);
    }


    private static void createListOfInputFilePathsRecursively(File inputFolder, List<String> inputFilePathsString) {
        File[] inputFiles = inputFolder.listFiles();

        if (inputFiles != null) {
            for (File inputFile : inputFiles) {
                if (inputFile.isDirectory()) {
                    // If the file is a directory, recursively list its content
                    createListOfInputFilePathsRecursively(inputFile, inputFilePathsString);
                } else {
                    // Get the path to each file as a String
                    String inputFilePathString = inputFile.getAbsolutePath();
                    inputFilePathsString.add(inputFilePathString);
                }
            }
        } else {
            System.out.println("No files found in the folder.");
        }
    }



    
    public static void main(String[] args) throws URISyntaxException {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String outputFolderPathString = args[0];

        Path outputFolderPath = Paths.get(outputFolderPathString);

        // Get the directory name
        String outputFolderName = outputFolderPath.getFileName().toString();
        
        Template3 template = new Template3(InteropFramework.getDefaultFactory());

        String date = outputFolderName;

        String currentWorkingDirectory = System.getProperty("user.dir");
        Path inputFolderPath = Paths.get(currentWorkingDirectory, "src", "main", "python", "model3", "data", date);
        String inputFolderPathString = inputFolderPath.toString();

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // try {
        //     // Read the JSON file and map it to the DataClass
        //     DataClass3 data = objectMapper.readValue(new File(inputFilePathString), DataClass3.class);

        //     Binding3 binding = new Binding3(template, data);
        //     binding.bind(outputFilePathString);

        // } catch (IOException e) {
        //     e.printStackTrace();
        // }



        try {

            File inputFolder = new File(inputFolderPathString);
            List<String> inputFilePathsString = new ArrayList<>();
    
            // Check if the provided path is a directory
            if (inputFolder.isDirectory()) {
                createListOfInputFilePathsRecursively(inputFolder, inputFilePathsString);
            } else {
                System.out.println("The provided path for input files folder is not a valid directory.");
            }
    
            // Iterate through all filepaths and create a binding file for each
            for (String inputFilePathString : inputFilePathsString) {
                // Read the JSON file and map it to the DataClass
                DataClass3 data = objectMapper.readValue(new File(inputFilePathString), DataClass3.class);
    
                Binding3 binding = new Binding3(template, data);

                // Using File class to get the filename of the input file
                File inputFile = new File(inputFilePathString);
                String inputFilename = inputFile.getName();

                // I want to remove the sequence of characters "data2_" from the origial filenames 
                // when creating the binding files
                String regexPattern = "data3_";
                Pattern pattern = Pattern.compile(regexPattern);
                Matcher matcher = pattern.matcher(inputFilename);
                String modifiedInputFilename = matcher.replaceFirst("");
                
                String outputFilename = String.format("binding3_%s", modifiedInputFilename);
                Path outputPath = Paths.get(outputFolderPathString, outputFilename);
                String outputPathString = outputPath.toString();

                binding.bind(outputPathString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
