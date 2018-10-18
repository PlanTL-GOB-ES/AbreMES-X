# SmaDB: Spanish Medical Abbreviation DataBase

## Introduction

This folder contains the SmaDB database. This database is based on the "Allie Search Service for Abbreviation / Long Form", developed by Yamamoto et al.[1], but contains Spanish abbreviations and their definitions instead of English.

These abbreviations are extracted from the titles and abstracts of different biomedical publications written in Spanish. The sources of these publications are SciELO[2], IBECS[3] and Pubmed[4]. We used a modified version of the algorithm developed by Schwartz & Hearst[5], adapted to the Spanish language. The structure of the database is very similar to the original Allie.

This repository includes the latest version of the database, and the source code available.

To create this database, we use XML files in Dublin Core format. We use the official ones from SciELO, and customized adaptations of the XML files to Dublin Core from IBECS and Pubmed metadata. These metadata are available at the MeSpEn website (http://temu.bsc.es/mespen). Learn more about Dublin Core in the following website: http://dublincore.org/

## Prerrequisites

This software has been compiled with Java SE 1.8, and should work with recent versions. You can download Java from the following website: https://www.java.com/en/download

Stanford CoreNLP is needed as well. We used version 3.9.0 for this work, and latest versions should work as well. Stanford CoreNLP is licensed under the GNU General Public License (v3 or later). You can download it from the following website: https://stanfordnlp.github.io/CoreNLP/

GeoNames is a geographical database which covers over eleven million placements. GeoNames is distributed under Creative Commons BY-4.0 license. It is available for download free or charge: http://www.geonames.org

## Directory structure

We can find the following folders in this project:

exec/

The executable to create the database.

exec/SmaDB_lib/

Stanford CoreNLP module, necessary to get the abbreviations.

SmaDB/

The complete database available for everyone.

src/

Source code used to generate de database.

src/resources/

Files needed to execute the database creator:
- corpora_list.txt: includes the folders where your corpora metadata are stored. The file needs to have one corpus per line. For each line, use the following format: "corpus_name {TAB} corpus_folder". Included corpora metadatas must follow the Dublin Core format.
- geonames.zip: includes all the world location names extracted from GeoNames.
- JST_spa.txt contains all Journal Subject Terms extracted from the Spanish edition of MeSH (Medical Subject Headings). 

## Usage

The executable file "SmaDB.jar" is the program you need to create your abbreviation-definition database. The executable needs one single parameter: the folder where the database will be stored. Before executing, you need to follow these simple steps:
- Create the output folder manually.
- Store inside that folder the files found at folder src/resources.

Remember that your corpora must be in Dublin Core format only. Learn more about Dublin Core in the following website: http://dublincore.org/

One your output directory is ready, just type the following command in your terminal:

$ java -jar SmaDB.jar RESOURCES_DIRECTORY OUTPUT_DIRECTORY

## Examples

$ java -jar SmaDB.jar ~/SmaDB/resources ~/SmaDB/DB

## Contact

Ander Intxaurrondo (ander.intxaurrondo@bsc.es)

## License

(This is so-called MIT/X License)

Copyright (c) 2017-2018 Secretaría de Estado para el Avance Digital (SEAD)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## References

[1] Y. Yamamoto, A. Yamaguchi, H. Bono and T. Takagi, "Allie: a database and a search service of abbreviations and long forms.", http://allie.dbcls.jp/

[2] Scientific Electronic Library Online (SciELO Spain) is an electronic virtual library covering a collection of Spanish health scientific journals selected following preestablished quality criteria. Developed and maintained by the Spanish National Library of Health Sciences.  http://scielo.isciii.es

[3] Bibliographical database for health articles, contains abstracts and references of different articles written in Spanish. Developed and maintained by the Carlos III Health Institute in Madrid, Spain. http://ibecs.isciii.es

[4] PubMed is a free search engine accessing primarily the MEDLINE database of references and abstracts on life sciences and biomedical topics. The United States National Library of Medicine (NLM) at the National Institutes of Health maintains the database as part of the Entrez system of information retrieval. https://www.ncbi.nlm.nih.gov/pubmed/

[5] A.S. Schwartz, M.A. Hearst, "A Simple Algorithm for Identifying Abbreviation Definitions in Biomedical Text"
