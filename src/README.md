This folder contains all the code used to create the database.

Users are permitted to change the code and adapt it to their needs without any restrictions, like adapting the abbreviation-definition extractor into other languages, domains, or other document types to analyze.

The folders are structured as follows:

## Directory structure

<pre>
abbreviation_extractors
Algorithms to extract abbreviations. Here we use a modified version of the algorithm developed by Schwartz & Hearst[1], adapted for the Spanish language, and using several filters to improve its quality.
Alternative abbreviation extraction algorithms should be added in this folder.

corpora_readers
This folder contains the code to read the folders where the corpus are stored. The program reads XML files in Dublin Core format only. 

elements
This folder contains the classes used for the generation of the database.

main
Executable class.

managers
The code in this folder manages the database to get co-abbreviations, and also to gather information about each article used to extract abbreviation-definition pairs.

tabular_file_readers
The code in this folder get the information from different database files. One file per database class/file.

tabular_files_writers
The code in this folder writes the information in the database. One file per database class/file.
</pre>

## References

[1] A.S. Schwartz, M.A. Hearst, "A Simple Algorithm for Identifying Abbreviation Definitions in Biomedical Text". 2003.
