#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Feb  8 21:05:52 2020

@author: ramgautam

"""

import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
import sklearn.model_selection as sk
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, f1_score, precision_score, recall_score

import string
import csv
import os
import sys
import pathlib

#making the list of the string words
#-----------------------------------

stopword_list = ["0o", "0s", "3a", "3b", "3d", "6b", "6o", "a", "A", "a1", "a2", 
                 "a3", "a4", "ab", "able", "about", "above", "abst", "ac", "accordance", 
                 "according", "accordingly", "across", "act", "actually", "ad", "added", "adj", 
                 "ae", "af", "affected", "affecting", "after", "afterwards", "ag", "again", "against", 
                 "ah", "ain", "aj", "al", "all", "allow", "allows", "almost", "alone", "along", 
                 "already", "also", "although", "always", "am", "among", "amongst", 
                 "amoungst", "amount", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore", 
                 "anyone", "anyway", "anyways", "anywhere", "ao", "ap", "apart", "apparently", 
                 "appreciate", "approximately", "ar", "are", "aren", "arent", "arise", 
                 "around", "as", "aside", "ask", "asking", "at", "au", "auth", "av", "available", 
                 "aw", "away", "awfully", "ax", "ay", "az", "b", "B", "b1", "b2", "b3", "ba", "back", 
                 "bc", "bd", "be", "became", "been", "before", "beforehand", "beginnings", "behind", 
                 "below", "beside", "besides", "best", "between", "beyond", "bi", "bill", "biol", "bj", 
                 "bk", "bl", "bn", "both", "bottom", "bp", "br", "brief", "briefly", "bs", "bt", "bu", 
                 "but", "bx", "by", "c", "C", "c1", "c2", "c3", "ca", "call", "came", "can", "cannot", 
                 "cant", "cc", "cd", "ce", "certain", "certainly", "cf", "cg", "ch", "ci", "cit", "cj", 
                 "cl", "clearly", "cm", "cn", "co", "com", "come", "comes", "con", "concerning", 
                 "consequently", "consider", "considering", "could", "couldn", "couldnt", 
                 "course", "cp", "cq", "cr", "cry", "cs", "ct", "cu", "cv", "cx", "cy", "cz", "d", 
                 "D", "d2", "da", "date", "dc", "dd", "de", "definitely", "describe", "described", 
                 "despite", "detail", "df", "di", "did", "didn", "dj", "dk", "dl", "do", "does", 
                 "doesn", "doing", "don", "done", "down", "downwards", "dp", "dr", "ds", "dt", "du", 
                 "due", "during", "dx", "dy", "e", "E", "e2", "e3", "ea", "each", "ec", "ed", 
                 "edu", "ee", "ef", "eg", "ei", "eight", "eighty", "either", "ej", "el", "eleven", 
                 "else", "elsewhere", "em", "en", "end", "ending", "enough", "entirely", "eo", 
                 "ep", "eq", "er", "es", "especially", "est", "et", "et-al", "etc", "eu", "ev", 
                 "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", 
                 "exactly", "example", "except", "ey", "f", "F", "f2", "fa", "far", "fc", "few", 
                 "ff", "fi", "fifteen", "fifth", "fify", "fill", "find", "fire", "five", "fix", 
                 "fj", "fl", "fn", "fo", "followed", "following", "follows", "for", "former", 
                 "formerly", "forth", "forty", "found", "four", "fr", "from", "front", "fs", "ft", 
                 "fu", "full", "further", "furthermore", "fy", "g", "G", "ga", "gave", "ge", "get", 
                 "gets", "getting", "gi", "give", "given", "gives", "giving", "gj", "gl", "go", 
                 "goes", "going", "gone", "got", "gotten", "gr", "greetings", "gs", "gy", "h", "H", 
                 "h2", "h3", "had", "hadn", "happens", "hardly", "has", "hasn", "hasnt", "have", 
                 "haven", "having", "hed", "hello", "help", "hence", "here", "hereafter", 
                 "hereby", "herein", "heres", "hereupon", "hes", "hh", "hi", "hid", "hither", 
                 "hj", "ho", "hopefully", "how", "howbeit", "however", "hr", "hs", "http", "hu", 
                 "hundred", "hy", "i2", "i3", "i4", "i6", "i7", "i8", "ia", "ib", "ibid", "ic", "id", "ie", "if", 
                 "ig", "ignored", "ih", "ii", "ij", "il", "im", "immediately", "in", "inasmuch", "inc", 
                 "indeed", "index", "indicate", "indicated", "indicates", "information", "inner", 
                 "insofar", "instead", "interest", "into", "inward", "io", "ip", "iq", "ir", "is", "isn", "it", 
                 "itd", "its", "iv", "ix", "iy", "iz", "j", "J", "jj", "jr", "js", "jt", "ju", "just", 
                 "k", "K", "ke", "keep", "keeps", "kept", "kg", "kj", "km", "ko", "l", "L", "l2", 
                 "la", "largely", "last", "lately", "later", "latter", "latterly", "lb", "lc", "le", 
                 "least", "les", "less", "lest", "let", "lets", "lf", "like", "liked", "likely",
                 "line", "little", "lj", "ll", "ln", "lo", "look", "looking", "looks", "los", "lr", 
                 "ls", "lt", "ltd", "m", "M", "m2", "ma", "made", "mainly", "make", "makes", "many", 
                 "may", "maybe", "me", "meantime", "meanwhile", "merely", "mg", "might", 
                 "mightn", "mill", "million", "mine", "miss", "ml", "mn", "mo", "more", "moreover", 
                 "most", "mostly", "move", "mr", "mrs", "ms", "mt", "mu", "much", "mug", 
                 "must", "mustn", "my", "n", "N", "n2", "na", "name", "namely", "nay", "nc", 
                 "nd", "ne", "near", "nearly", "necessarily", "neither", "nevertheless", "new", 
                 "next", "ng", "ni", "nine", "ninety", "nj", "nl", "nn", "no", "nobody", 
                 "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not", 
                 "noted", "novel", "now", "nowhere", "nr", "ns", "nt", "ny", "o", "O", "oa", 
                 "ob", "obtain", "obtained", "obviously", "oc", "od", "of", "off", "often", 
                 "og", "oh", "oi", "oj", "ok", "okay", "ol", "old", "om", "omitted", "on", "once", 
                 "one", "ones", "only", "onto", "oo", "op", "oq", "or", "ord", "os", "ot", 
                 "otherwise", "ou", "ought", "our", "out", "outside", "over", "overall", "ow", 
                 "owing", "own", "ox", "oz", "p", "P", "p1", "p2", "p3", "page", "pagecount", 
                 "pages", "par", "part", "particular", "particularly", "pas", "past", "pc", "pd", 
                 "pe", "per", "perhaps", "pf", "ph", "pi", "pj", "pk", "pl", "placed", "please", 
                 "plus", "pm", "pn", "po", "poorly", "pp", "pq", "pr", 
                 "predominantly", "presumably", "previously", "primarily", "probably", 
                 "promptly", "proud", "provides", "ps", "pt", "pu", "put", "py", 
                 "q", "Q", "qj", "qu", "que", "quickly", "quite", "qv", "r", "R", "r2", "ra", 
                 "ran", "rather", "rc", "rd", "re", "readily", "really", "reasonably", 
                 "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related", 
                 "relatively", "research-articl", "respectively", "resulted", "resulting", 
                 "results", "rf", "rh", "ri", "right", "rj", "rl", "rm", "rn", "ro", "rq", 
                 "rr", "rs", "rt", "ru", "run", "rv", "ry", "s", "S", "s2", "sa", "said", "saw", 
                 "say", "saying", "says", "sc", "sd", "se", "sec", "second", "secondly", 
                 "section", "seem", "seemed", "seeming", "seems", "seen", "sent", "seven", 
                 "several", "sf", "shall", "shan", "shed", "shes", "show", "showed", 
                 "shown", "showns", "shows", "si", "side", "since", "sincere", "six", "sixty", 
                 "sj", "sl", "slightly", "sm", "sn", "so", "some", "somehow", "somethan", 
                 "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "sp", 
                 "specifically", "specified", "specify", "specifying", "sq", "sr", "ss", 
                 "st", "still", "stop", "strongly", "sub", "substantially", "successfully", "such", 
                 "sufficiently", "suggest", "sup", "sure", "sy", "sz", "t", "T", "t1", "t2", 
                 "t3", "take", "taken", "taking", "tb", "tc", "td", "te", 
                 "tell", "ten", "tends", "tf", "th", "than", "thank", "thanks", "thanx", "that", 
                 "thats", "the", "their", "theirs", "them", 
                 "themselves", "then", "thence", "there", "thereafter", "thereby", "thered", 
                 "therefore", "therein", "thereof", "therere", 
                 "theres", "thereto", "thereupon", "these", "they", "theyd", "theyre", "thickv", 
                 "thin", "think", "third", "this", "thorough", 
                 "thoroughly", "those", "thou", "though", "thoughh", "thousand", "three", 
                 "throug", "through", "throughout", "thru", "thus", "ti", 
                 "til", "tip", "tj", "tl", "tm", "tn", "to", "together", "too", "took", "top", 
                 "toward", "towards", "tp", "tq", "tr", "tried", "tries", "truly", "try", 
                 "trying", "ts", "tt", "tv", "twelve", "twenty", "twice", "two", "tx", "u", "U", 
                 "u201d", "ue", "ui", "uj", "uk", "um", "un", "under", "unfortunately", "unless", 
                 "unlike", "unlikely", "until", "unto", "uo", "up", "upon", "ups", 
                 "ur", "us", "used", "useful", "usefully", "usefulness", "using", "usually", 
                 "ut", "v", "V", "va", "various", "vd", "ve", "very", "via", "viz", "vj", "vo", 
                 "vol", "vols", "volumtype", "vq", "vs", "vt", "vu", "w", "W", "wa", "was", "wasn", 
                 "wasnt", "way", "we", "wed", "welcome", "well", "well-b", "went", "were", "weren", 
                 "werent", "what", "whatever", "whats", "when", "whence", "whenever", "where", 
                 "whereafter", "whereas", "whereby", "wherein", "wheres", "whereupon", "wherever", 
                 "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole", 
                 "whom", "whomever", "whos", "whose", "why", "wi", "widely", "with", "within", 
                 "without", "wo", "won", "wonder", "wont", "would", "wouldn", "wouldnt", "www", 
                 "x", "X", "x1", "x2", "x3", "xf", "xi", "xj", "xk", "xl", "xn", "xo", "xs", "xt", 
                 "xv", "xx", "y", "Y", "y2", "yes", "yet", "yj", "yl", "you", "youd", 
                 "your", "youre", "yours", "yr", "ys", "yt", "z", "Z", "zero", "zi", "zz"]




#----------------------------------------
#method for text cleaning ---------------

def processing_text (line):

    # Check characters to see if they are in punctuation
    remove_punc = [char for char in line if char not in string.punctuation]
    
    # Join the characters again to form the string.
    remove_punc = ''.join(remove_punc)
            
    # Now just remove any stopwords
    #stopword_removed = [word for word in remove_punc.split() if word.lower() not in stopwords.words('english')]
    
    clean_list = [word for word in remove_punc.split() if word.lower() not in stopword_list]
    clean_list_final = ' '.join(clean_list)
    
    return clean_list_final


#classify the features bases on textbooks 
#classify either from nobility house or not
def nobility_classfier(death_row_name, lineToSearch):

    #just looking into first value
    character = death_row_name[0].lower()
    first_value = character.split()[0]

    #listing the nobel houses in game of thrones
    noble_houses_list = ["stark", "arryn", "baratheon", "tully", "greyjoy",
                     "lannister", "tyrell", "martell", "targaryen"]

    count = 0
    if first_value in lineToSearch:
        counts_dic = dict()
        words = lineToSearch.split()

        for word in words:
            if word in counts_dic:
                counts_dic[word] += 1
            else:
                counts_dic[word] = 1

        #print(counts_dic)

        for noble in noble_houses_list:
            if noble in counts_dic:
                count = counts_dic[noble]

    if count > 0:
        #print(" noble ")
        return 1
    else:
        #print (" not noble ")
        return 0

   
#----------------------------
def gender_classifer(death_row_name, lineToSearch):

    #just looking into first value
    character = death_row_name[0].lower()
    first_value = character.split()[0]
    
    #print("value to search: ", first_value)
    #print (lineToSearch)
    
    inmale_count = 0
    infemale_count = 0
    
    if first_value in lineToSearch: 

        counts_dic = dict()
        words = lineToSearch.split()
        
        for word in words:
            if word in counts_dic:
                counts_dic[word] += 1
            else:
                counts_dic[word] = 1
        
        #print (counts_dic)

        #male list identifier 
        male_list = ['man','he', 'him', 'handsome', 'boy', 'king', 'prince']
        for male in male_list:
            if male in counts_dic:
                inmale_count = counts_dic[male]
        
        #female list identifier
        female_list = ['she', 'baby', 'cute', 'bird', 'girl', 'honey', 'princess']
        for female in female_list:
            if female in counts_dic:
                infemale_count = counts_dic[female]
    
    #print("male count ", inmale_count)
    if(infemale_count > 0):
        #print("female count ", infemale_count)
        pass
    
    if(inmale_count > 0):
        #print("male count ", infemale_count)
        pass
        
    return inmale_count, infemale_count

#--------------------------
#this method is checking either the character is mentioned
#inside the book or not ------
def inbook_classifier(death_row_name, lineToSearch):

    character = death_row_name[0].lower()
    first_value = character.split()[0]

    #checking the character presence inside the book
    if first_value in lineToSearch:
        return 1
    else:
        return 0


#----------------------------
#this methods gives the count of the character inside book
def character_frequency (death_row_name, lineToSearch):
    count = 0

    character = death_row_name[0].lower()
    first_value = character.split()[0]

    if first_value in lineToSearch:

        counts_dic = dict()
        words = lineToSearch.split()

        for word in words:
            if word in counts_dic:
                counts_dic[word] += 1
            else:
                counts_dic[word] = 1

        #print (counts_dic)
        if first_value in counts_dic:
            count = counts_dic[first_value]

    return count


def dead_alive_classifier(death_row_name, lineToSearch):
    #just looking into first value
    character = death_row_name[0].lower()
    first_value = character.split()[0]
    
    death_count = 0
    life_count = 0
    
    if first_value in lineToSearch: 

        counts_dic = dict()
        words = lineToSearch.split()
        
        for word in words:
            if word in counts_dic:
                counts_dic[word] += 1
            else:
                counts_dic[word] = 1
        
        #print (counts_dic)

        #death list identifier 
        death_list = ["kill", "killed", "die", "death", "demise", "died"]
        for die in death_list:
            if die in counts_dic:
                death_count = counts_dic[die]
        
        #alive list identifier
        alive_list = ["life"]
        for life in alive_list:
            if life in counts_dic:
                life_count = counts_dic[life]

    if(death_count > 0):
        #print("death_count ", death_count)
        pass
    
    if(life_count > 0):
        #print("life_count ", life_count)
        pass
        
    return life_count, death_count
        
#classification methods ends 

#method for extracting the features from the text book 
#the machine learning algorithm is then run for evaluation metrices
    
def feature_extraction_textbook(features_array,death_row_name):

    #opening the first book for reading
    #-----------------------------------
    try:
        text_book1 = open("001ssb.txt", "rb")

        text_book1_list = []

        for line in text_book1:
            line = line.lower()
            stopword_removed = processing_text (str(line))
            text_book1_list.append(stopword_removed)

    except IOError:
        print("001ssb.txt " + "not available in " + os.path.abspath(os.getcwd()))

    finally:
        text_book1.close()

    #text_book1_list = []


    #opening the second book for reading
    #-----------------------------------
    try:
        text_book2 = open("002ssb.txt", "rb")
        text_book2_list = []

        for line in text_book2:
            line = line.lower()
            stopword_removed = processing_text (str(line))
            text_book2_list.append(stopword_removed)

    except IOError:
        print("002ssb.txt " + "not available in " + os.path.abspath(os.getcwd()))

    finally:
        text_book2.close()

    #text_book2_list = []


    #opening the third book for reading
    #-----------------------------------
    try:
        text_book3 = open("003ssb.txt", "rb")

        text_book3_list = []

        for line in text_book3:
            line = line.lower()
            stopword_removed = processing_text (str(line))
            text_book3_list.append(stopword_removed)

    except IOError:
        print("003ssb.txt " + "not available in " + os.path.abspath(os.getcwd()))

    finally:
        text_book3.close()

    text_book3_list = []

    #opening the fourth book for reading
    #------------------------------------
    try:
        text_book4 = open("004ssb.txt", "rb")
        text_book4_list = []

        for line in text_book4:
            line = line.lower()
            stopword_removed = processing_text (str(line))
            text_book4_list.append(stopword_removed)

    except IOError:
        print("004ssb.txt " + "not available in " + os.path.abspath(os.getcwd()))
        sys.exit()

    finally:
        text_book4.close()

    text_book4_list = []

    #opening the fifth textbook for reading
    #----------------------------------
    try:
        text_book5 = open("005ssb.txt", "rb")
        text_book5_list = []
        for line in text_book5:
            line = line.lower()
            stopword_removed = processing_text (str(line))
            text_book5_list.append(stopword_removed)

    except IOError:
        print("005ssb.txt " + "not available in " + os.path.abspath(os.getcwd()))
        sys.exit()

    finally:
        text_book5.close()

    text_book5_list = []

    #print(len(death_row_name))

    #opening the csv file for writing
    file_csv = open("textbased_features.csv", "w")

    line_out = "Name" + "," + "Gender" + "," + "Nobility" + ","  + "book1" + "," + "book2" + ","
    line_out = line_out + "book3" + "," + "book4" + "," + "book5" + ","
    line_out = line_out + "frequency" + "," + "dead or alive\n"
    file_csv.write(line_out)


    #looking into the first book
    for i in range (len(death_row_name)):

        #assuming all female
        gender = 0

        #assuming all dead
        life = 0

        male_count = 0
        female_count = 0

        #assuming none are noble
        nobility = 0

        #life and death count
        life_count = 0
        death_count = 0

        #flag  for present in book
        # #initially assumed that not present in any book
        b1 = 0
        b2 = 0
        b3 = 0
        b4 = 0
        b5 = 0

        #initially assuming cha
        total_char_frequency = 0

        #running through first book ------
        for line_list in text_book1_list:

            #gender classifer fucntion
            mc, fc = gender_classifer (death_row_name[i], line_list)
            male_count = male_count + mc
            female_count = female_count + fc

            #nobility classifier function - check either noble or not
            if nobility == 0:
                nobility = nobility_classfier(death_row_name[i], line_list)
            
            #life and death classifier function
            lc, dc = dead_alive_classifier(death_row_name[i], line_list)
            life_count = life_count + lc 
            death_count = death_count + dc

            #classify of the character is present in the book or not
            if b1 == 0:
                b1 = inbook_classifier(death_row_name[i], line_list)

            #character frequency
            char_frequency = character_frequency (death_row_name[i], line_list)
            total_char_frequency = total_char_frequency + char_frequency


        #go through the second book and get all the features
        for line_list in text_book2_list:

            mc, fc = gender_classifer (death_row_name[i], line_list)
            male_count = male_count + mc
            female_count = female_count + fc

            #nobility classifier function - check either noble or not
            if nobility == 0:
                nobility = nobility_classfier(death_row_name[i], line_list)

            #life and death classifier function
            lc, dc = dead_alive_classifier(death_row_name[i], line_list)
            life_count = life_count + lc
            death_count = death_count + dc

            if b2 == 0:
                b2 = inbook_classifier(death_row_name[i], line_list)

            #character frequency
            char_frequency = character_frequency (death_row_name[i], line_list)
            total_char_frequency = total_char_frequency + char_frequency

        #go through the third book and get all the features
        #--------------------------
        for line_list in text_book3_list:

            mc, fc = gender_classifer (death_row_name[i], line_list)
            male_count = male_count + mc
            female_count = female_count + fc

            #nobility classifier function - check either noble or not

            if nobility == 0:
                nobility = nobility_classfier(death_row_name[i], line_list)

            #life and death classifier function

            lc, dc = dead_alive_classifier(death_row_name[i], line_list)
            life_count = life_count + lc
            death_count = death_count + dc

            if b3 == 0:
                b3 = inbook_classifier(death_row_name[i], line_list)

            #character frequency

            char_frequency = character_frequency (death_row_name[i], line_list)
            total_char_frequency = total_char_frequency + char_frequency

        #go through the fourth book and get all the features
        #---------------------------------------------------
        for line_list in text_book4_list:

            mc, fc = gender_classifer (death_row_name[i], line_list)
            male_count = male_count + mc
            female_count = female_count + fc

            #nobility classifier function - check either noble or not
            if nobility == 0:
                nobility = nobility_classfier(death_row_name[i], line_list)

            #life and death classifier function
            lc, dc = dead_alive_classifier(death_row_name[i], line_list)
            life_count = life_count + lc
            death_count = death_count + dc

            if b4 == 0:
                b4 = inbook_classifier(death_row_name[i], line_list)

            #character frequency
            char_frequency = character_frequency (death_row_name[i], line_list)
            total_char_frequency = total_char_frequency + char_frequency

        #go through the fifth book and get all the features
        #-------------------------------------------------

        for line_list in text_book5_list:

            mc, fc = gender_classifer (death_row_name[i], line_list)
            male_count = male_count + mc
            female_count = female_count + fc

            #nobility classifier function - check either noble or not

            if nobility == 0:
                nobility = nobility_classfier(death_row_name[i], line_list)

            #life and death classifier function

            lc, dc = dead_alive_classifier(death_row_name[i], line_list)
            life_count = life_count + lc
            death_count = death_count + dc

            if b5 == 0:
                b5 = inbook_classifier(death_row_name[i], line_list)

            #character frequency
            char_frequency = character_frequency (death_row_name[i], line_list)
            total_char_frequency = total_char_frequency + char_frequency

            
        #determining the gender based on text classification count
        if male_count > female_count:
            #print(i, " ", death_row_name[i], " ", "male count: ", male_count)
            gender = 1
        else:
            #print(i , " ", death_row_name[i], " ", "female count: ", female_count)
            gender = 0
        
        #determining life and death based on text classification count
        if death_count > life_count:
            life = 0
            #print("life ", life)
        else:
            life = 1
        
        line_out = str(death_row_name[i]) + "," + str(gender) + "," + str(nobility) + ","
        line_out = line_out + str(b1) + "," + str(b2) + "," + str(b3) + "," + str(b4) + ","
        line_out = line_out + str(b5) + "," + str(total_char_frequency) + "," + str(life)

        file_csv.write(line_out + "\n")

    file_csv.close()


#this function writes into the run file
def write_to_runFile(y_pred, death_row_name, filename):
    #opening the file to write 
    f = open(filename + "logRegression.run", "w")

    #print(y_pred)

    for i in range(len(death_row_name)):
        #print (i+1 , " ", death_row_name[i], " ", y_pred[i])
        #f.write(str(i+1) + " " + str(*death_row_name[i]) + " " + str(*y_pred[i]) + "\n")

        death_char = str(*death_row_name[i]).replace(" ", "_")
        death_char = death_char.replace("-", "_")

        if y_pred[i] == 0:
            f.write("0" + " " + "0" + " " + death_char + " " + str(i+1) + " " +  "1" + " " + "team3" + "\n")

    f.close()


def main():
    
    #taking the data file from here - It can be any python path
    data = 'character-deaths.csv'
    death = pd.read_csv(data)
    
    #removing all the invalid or empty column from Intro Chapter
    death = death[death['Book Intro Chapter'].notnull()]

    #print(death.columns)
    #print(len(death))
    
    #if death column is not null, the chatacter is still alive
    death['dead or alive']=death['Death Year'].notnull().astype('int')
    #print(len(death['dead or alive']))
    
    death_features = pd.DataFrame ({'Gender' : death['Gender'],'Nobility' : death['Nobility'], 'GoT':death['GoT'],
                               'CoK':death['CoK'],'SoS':death['SoS'],'FfC':death['FfC'],'DwD':death['DwD'],
                               'dead or alive' :death['dead or alive']})
    #print(len(death_features))
    X = death_features[['Gender', 'Nobility', 'GoT', 'CoK', 'SoS', 'FfC', 'DwD']]
    y = death_features['dead or alive']
    
    #splitting the data into training and testing dataset - 70% and 30%
    X_train, X_test, y_train, y_test = sk.train_test_split(X, y, test_size = 0.3, random_state=101)
    
    #defining the logistic model
    logModel = LogisticRegression()
    
    #train the model
    logModel.fit(X_train, y_train)
    
    y_pred = logModel.predict(X)

    org_y_pred = y_pred

    #converting it into row column
    y_pred = y_pred.reshape((-1, 1))
    
    #characters prediction
    #print(y_pred)
    #print(y_pred.shape)
   
    #predicted characters
    death_row_name = (death['Name'])
    death_row_name = death_row_name.values.reshape(-1,1)
    
    #creating run file if needed
    write_to_runFile(org_y_pred, death_row_name,"")
    
    #print(death_row_name.shape)
    alive_count = 0
    dead_count = 0 
    for x in range(len(death_row_name)):
        if(y_pred[x] == 1):
            alive_count = alive_count + 1
            #print (x+1 , " ", death_row_name[x], " ", y_pred[x])
        else:
            dead_count = dead_count + 1

    #--------- RESULTS BASED ON CSV FILE -----------
    print("--------------")
    print("Total alive prediction count : %1.0f" % alive_count)
    print("Total  death prediction count : %1.0f" % dead_count)
    print("Accuracy: %1.4f" % accuracy_score(y, y_pred))
    print("Precision: %1.4f" % precision_score(y, y_pred))
    print("Recall: %1.4f" % recall_score(y, y_pred))
    print("F1 Score: %1.4f\n" % f1_score(y, y_pred))
    
    #--------- RESULTS BASED ON CSV FILE ENDS -----------
    
    #-------- RESULT BASED IN TEXT BOOK STARTS ---------
    #array to store features
    rows, cols = (len(death_row_name), 9)

    # initialize all array values with 0
    features_array = [[0]*cols]*rows

    #call the function for feature extraction 
    #feature_extraction_textbook(features_array,death_row_name)

    #check if the textbased features is already available
    path = pathlib.Path('textbased_features.csv')
    if path.exists():
        print(str(path) + " file already exists not need to creat features again ")
    else:
        feature_extraction_textbook(features_array,death_row_name)

    #now using machine learning algorithms to text based features
    features_data = 'textbased_features.csv'
    text_death = pd.read_csv(features_data)

    text_features = pd.DataFrame ({'Gender' : text_death['Gender'],'Nobility' : text_death['Nobility'], 'book1':text_death['book1'],
                                    'book2':text_death['book2'],'book3':text_death['book3'],'book4':text_death['book4'],'book5':text_death['book5'],
                                   'frequency' :text_death['frequency'], 'dead or alive' :text_death['dead or alive']})

    #print(text_features)
    #print(len(death_features))
    text_X = text_features[['Gender', 'Nobility', 'book1', 'book2', 'book3', 'book4', 'book5','frequency']]
    text_y = text_features['dead or alive']

    #splitting the data into training and testing dataset - 70% and 30%
    text_X_train, text_X_test, text_y_train, text_y_test = sk.train_test_split(text_X, text_y, test_size = 0.3, random_state=101)

    #defining the logistic model
    text_logModel = LogisticRegression()

    #train the model
    text_logModel.fit(text_X_train, text_y_train)

    text_y_pred = text_logModel.predict(text_X)

    org_text_y_pred = text_y_pred

    #converting it into row column
    text_y_pred = text_y_pred.reshape((-1, 1))

    #creating run file if needed
    write_to_runFile(org_text_y_pred, death_row_name, "textbook_")

    #print(text_y_pred)

    #print(death_row_name.shape)
    #text_alive_count = 0
    #text_dead_count = 0
    #for x in range(len(death_row_name)):
        #if(text_y_pred[x] == 1):
            #text_alive_count = text_alive_count + 1
        #else:
            #text_dead_count = text_dead_count + 1

    print("--------------")
    #print("Total text alive prediction count : %1.0f" % text_alive_count)
    #print("Total  text death prediction count : %1.0f" % text_dead_count)
    print("Accuracy text : %1.4f" % accuracy_score(text_y, text_y_pred))
    print("Precision text: %1.4f" % precision_score(text_y, text_y_pred))
    print("Recall text : %1.4f" % recall_score(text_y, text_y_pred))
    print("F1 Score text : %1.4f\n" % f1_score(text_y, text_y_pred))
    
if __name__ == "__main__":
    main()
    