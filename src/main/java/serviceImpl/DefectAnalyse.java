package main.java.serviceImpl;

import main.java.daoImpl.AnalyseDaoImpl;
import main.java.daoImpl.GeneDaoImpl;
import main.java.model.Dna;
import main.java.service.DefectAnalyseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefectAnalyse implements DefectAnalyseService {

    private Map<String, String> dataMap;
    private DefectRecognition defectRecognition;
    private AnalyseDaoImpl analyseDaoImpl;
    private GeneDaoImpl geneDao;
    private String stdGene;
    private String stdCDS;
    private ArrayList<String> CDS;

    //TODO 目前只针对LPL
    private final String LPL = "AAATTTTTCCGTCTGCCCTTTCCCCCTCTTCTCGTTGGCAGGGTTGATCCTCATTACTGTTTGCTCAAACGTTTAGAAGTGAATTTAGGTCCCTCCCCCCAACTTATGATTTTATAGCCAATAGGTGATGAGGTTTATTTGCATATTTCCAGTCACATAAGCAGCCTTGGCGTGAAAACAGTGTCAGACTCGATTCCCCCTCTTCCTCCTCCTCAAGGGAAAGCTGCCCACTTCTAGCTGCCCTGCCATCCCCTTTAAAGGGCGACTTGCTCAGCGCCAAACCGCGGCTCCAGCCCTCTCCAGCCTCCGGCTCAGCCGGCTCATCAGTCGGTCCGCGCCTTGCAGCTCCTCCAGAGGGACGCGCCCCGAGATGGAGAGCAAAGCCCTGCTCGTGCTGACTCTGGCCGTGTGGCTCCAGAGTCTGACCGCCTCCCGCGGAGGGGTGGCCGCCGCCGACCGTAAGTTTTGCGCGCAAACTCCCCTCCACCTGCAGACCCGGCGGGTGGCCACTGCCACCCGAACTGAGGATGAGAAGAAGGAAGTTGGAAGGGGCGGTGGATGCGCCCAGGGACTCTCCCAGCCTGGGCTCTAGCCCCGAAACGGTCCCCGGAGTGGGATCCAGGAGGGGCCGGGAGGGAATCTCCTCCCGATCGTGAAGCGGCGGCGCCCAGTTCCCGCTTTTTCTCTCTGCCGGGTTCCCGCGCTATCCCTTCCACTCTGGCTGGGACCGCGTTCCCGGGCTCGCAGGCTCCGCCGGGGAGGTTCCGGGGTGTGGGGGCCGGGACGGCGGAGGCGGGGAGTAAGGGCCCGGCTGGCGGTGACCTGCAGTCACCTCTCTGCCGGAGGGGCCCTGGAATGAAAGGCGCGCGGGCCAAGGTGACCTCGCCTTGGTTGGCACTGCGGCTCAGCCCCCGCCCGGGGACTCGCGGGCCGACTGTGGCCCCTTCTGGGGAAGCCGGGGCGCGGGGAGGCGTTCCGGGCATCTCAGCCGCACGGGGTACGCTCGCCCTCGGCGGGGCCCCTCGCTCCGCTGTGGGAGTGGCAGTGGGTGTCGGGGTGGAGAAAGTACGCGTGGCGCGGAGTCCTGGGGACGCGGCGTCCCACCCGCTCTGGGGAGCCCCGGACTCTCTCCAGCTTCCAGGCTCGCATGCCCCTCTTTTCTTAGTGCCCTGAGAACCCAGCGAGGGGCTGACCCTCCCGAAACCGTGGCGCAGCCACCAGCAATCTGTGGTCGCCGACTCGGGGGGTTGCCAGGTCTGCGTTTGGCCACCCTTTCTGTCCTGGGGGCTGAGGTCAGCTCCGGGCGCCCGGCCCCGCCGCGCGGCTGCGAGCACGTGGGGTTGACGGGCGCCGCGTGGAGGCAGCGAGCACAACGGTGGTCACCGCCGCCAGGGAACCGCCCGCTCGCTGGGGTCCAGGCGTTCGGGGCCAAATGAGAATGTCTCAGACCTGTCCGCAATGGAGGCAGCCTGCTTAATTCGAACCTCGATTCAGTAAACATGCAACAGCAGCATAGAGAGCAGCTGAAGCCATTCATAACACGGGACAACATTTCCTTTTTTCTTCCATGCTGGAATTGCAATTAGGGCGGTGTCGCTTGGATGTGCTCTCAGGCGGCACGTCCCCAGCGGTTCAAGTTATAATAGAGTCTCTCCATAGCTTTGATGGCCGCTAAACGTTTGTTTTATTTTGGCATTAATTTGTGAAACATTTTTGTTAGGTTAAAAAACAAAAAGTTGGCAGGGAGCAGTGGCTCACCCATGTAGTCCCAGCACTTCGGGAGGCCGAGGCCGGAAGACAGCTTGAGCCCAGAAGTTCGAAATCAGCCTGGGCAACATAGGGAGACACCGTCTCTATAAAAAAAAAATACAAAAATTAGCCGGGCGTGGTGGCCTGTGCCTGTGGTCCCAGATAGTCAGCAGGCTGAGAGATCACTTGAGCCAGGGAGTTTCAGTAGACTGCAGTGAGCTGTGATTGCCCCACTGCACTTCATCCTGGGTGACAGTGAGACCCTGTCTCATAAAATTAAAACAAAACAAAACAAACACTAAGTATAGATTCCATCAAAGCAAAATTGGATAAGAAAAAAAGTATCTTTTCTATTGGATCAGTTTGAAAACACTGGAGAGTTGATGAGAAAGTCTTCAACATCTTAGGTGGGGTATGTTTCGTATGTTTCCCTTCGTACTGCTTAATGCTGACAAGAAGATGGTAGGAGCCAACTCCAAATTCTTATTTCAGAAAGCACACCATAGAATAACGTCATTTTCATTGCAAAACAAGCACCGAAATATGTCATCACATTCAAGTTTTTCCTAGGCTCCCTTACAGGTTCAAGATCCTAAATTCTTGGGAGTCAGTGTCACCTCTCTGGGTTTAGGTTCCTCAACTCTGCAATGAGTTTGGATGAGGCCAATGTTCTCTGAGCCTGGTGTAACTCTTGCCTCTTTAAGTGGACACTTATGTGATTAATTAGTTTAATTGAGTTGTAGCCAACACATGCTTTTCCTAGCTGTAAATATATTAAGGAAGGATTATTTCCAAGTAGACTGGAAACGATGCCCTCCCATCCCCTCCACTTTCACTCTACTCACCCAATATATCATGCCTCTCCCATCACAGCAACTTTCTCCCTCTTTCTCCTCCAGATGCATTCATCTAGGAAGGTAAGAATTTCAGGGAGAGAAAGATGTCACCGTGGTAGAAAGACAGGGATCACCTCCTCTGGGCTCTTGAGTTTACTTATTCATTCTGGATTCTTTCTAACAAGAATATGAGGACAAGAGGCACTGTCCTCAGGCACTTCGTCCTGGGAGCCACCACCATCTCTGCATGGCCCCAATTAGGAAACGTGAAGAGCTAGGAGAGGGAGAGTATGGTCAGTGCTTAGCAGCTGAAGTTCCACTTGCCTGGCCATCGTGAATTTCCAGGCTGTCTTCTGAGTTGAACATGATGGCAAAGGAGAGCAAAATAGCAGATGTCACTGAAGGAGAGCTCAGCGAGGGAGTGATTGATTAATAGCTGTATTGAAAGGTGGGAGTCAGGTACGGGGGAAGAGCGGCGATGGAAAATTTTCGCTTTCTTTCAGCAGCTTATTTTTAACTCAGCTTTCTGTTCTTGCTTTATTATGGAGGAAAAATTGGGCCATAGAGTTTACTGCCTTATGCCAGATTGTTCAAGAAAATGCCTTGCAACTTACAATATTTTGCAGCTAGTTTCTTCCGTGACCACCACAAAGACTGCATTGACTTAAATATGAAGATGTTCCAGCCATCAAAATGATGGTTGGTGATGATTTTGGATCACAAAGTGTAAGGAAAGTATTCAAGACATGAGTATCATGATTTTTTAAGGTCTGGATGAAGAGACCATTTGGATTTACTAATAAGGTAAATTCCAACTTTTATGGCAATAAAAACAACAAAAACACTTATCAGTGTAAAGCTTTGGGATCATCTATCCATTAAATGAGTTTGTCACCACAGTGAACTAAATACCTTTTATCAACAGAGACTTTCTAACCTGGGAGTAAAATTCTTGTCACAGTGCTTTTGTCACATTCTGTCTTTGCAAAAGTTGAAGGCTCCAATAGTTTCTGAAGGACTAATAGGATAGGGTTCCAACTTACTCAGAGGCTAAGAGTTTGAAATTTACTCTGAACGATGTCTGTTCACTAGACTGCGTGACTGCAGTTTGCTGTCTTGTGGCCATTTTAAGATTGTCTGTGTGCACTGACACCATTTGCGTACTCAACAACAGGTATCTACTAGGAAGGAAGGAATGGATTATCTTAGGTGCTATATATATATGTAAGTTCTGCCGGAAGTGAGCCTATTAAACTTGTGCCAATTCATTCCTTTTGCTCTTCCCAGTCTGTGCTCTCAGAATGATCAAATGCTATCTAAGTAGTGTGGTGATTTTGACTGTTTGAATGAATAAACGAAAACGTCTCCCACCATTATTTGACAGTAGAATAGAACAAAGATGACCCAGTTGGCGTGGCCACACTGTGTCTAATCCAGCCACCTTGCTCACAGCACTCAACCCACTTTGGTGTGTGCGCTCATCTGTATTTTCGTAAATGTTGAAGTCTCTTTCTATGCAGTCAGGTAGGGGTAATACCATTCTGGCTTGGATTAATTTGAGTGTAGTACAAAGTTTAGAGAAGCTTTTAAGTAGCATGAAAAGTCAGATGCTTTCGGAGGGATGGTGGTGAATGTAGGTAAATGGATCTGCACTTAGAGATCCTCAACAGCCCTTCATTCAAGATACAGCTATCATGACATCAATAATGTACCTTTGAAAAAAAATTGGCTGCAGAATTAATTACGGTGAAAAACAGTATAGGAGTTAGCACTTACATTTTAACTAAAAAGAATAGCGTCCCCATGTTTATTCAGCCCTCCCTCCAATAAAACAATTGTTGGCAAAGTAATCATGGACTTTCATTGTGTTAGTTTGAGACAACTGGATGTTTCCATTTGCCATCCTCAGCAACAAGAAGAAAGTAGTCTCAGATTAACCAGGTAAGTATTTTGTATTTCACTTAAAAAATCTTGACTGAAATATGCCTATGTGATGACACTCAACCAAATTATTTCATTAACCAGTCCACAAGATCTCCTTAAATAATGATGGCTTATTCACACTTGATGGTCTCATTCAGTGGGGCAATTTTAATACACATCTCTGAACCTATTTTTTAACCCCTCTTTTTCAGTAGTGTGGAAGGTTAGCCCTAATATTGGAGAAAATTCAGGGTAAAATTCAGATGATTCATACAGGATTTATTTTTCCTATTCCATTAATAAAACAACTTTTATAAAAATAAAAAGTAGGCTGGCACAGTGGCTCACTCCTGTAATCCCAGCATTTTGGGAGGCCGAGGCGGGTGGATCATGAGGTCGGGAGTTCAAGACCAGCGTGGCCAGGATGGTGAAACCCCATCTCTACTAAAAATACAAAAATTAGCCAGGCGTGGTGGCAGGCACCTGTAATCCCGGCTACTCGGGAGGCTGAGGCAGAGAATGGCTTGAACCCAGGAGGCATAGGTTGCAGTGAGTCACGATCGTGCCACTGCACTCCAGCCTGGGTGACAGAGCAAGACTCCGTTTCAAAAAATAATAATAAAATAAAATGAAATAAAGTAAAGCTGCATGTTAGAGAAGTCAAGAGCATTACTTACGTTAGAATATCTGAACAGACCAATCAATTCAGTCTGATCATGATATTGATGTTTTTCTTCACCAAATCGACCACATCAGTAATTCACTTTGTTCTTCGATATCCTACAGACACTGCCTGAGTCGATAACTATGACTATCAGTCTCAGAGAGCAAATGAATTACTGAGGAAGCCCTGTAGGAGTGAGAGAAAAGGGGGGAGAGAGAGAGAAAGGGGTGGGGGGATAACAGGAGAACAGAAATTCCAAAGAGAATTGCATTCTCATTGAGTTCTTGTACCTCATGTCATTGCATAAATGTTCATCTTACTCACGTGATGACTTTGATCTGCCTTTAAAGCACCATCTGCTGCTTTCCTGGGATGCTCAACACTTCCCTCTTTCTAGCAACAAGAATTACCACTCTTCCCCTCTATACATTTATCTTTCTCTACGTGCTTTAACTTCTCAGCCTAATTTCGTCTCTGTGAGTTATTATCTATGTTAGAATAAATTCTTTGTCTTTGTTTACACACTCAGATTTGTAGTTATTTATTTAGGAATTTAGGAATAAAGATTCCATAGTCAGGAAAGGCACAATTTATAACTTGCGTGTTACCCAAAACTCTCCCCTAAGGGCTTAATATGGACATTTCTGACGAGGCCTGATGGGCAGGTGGTACGGTGATGCTAAGTTAAATTCAGAATGAAGGCCTGCCTTTCCTTCCCTCCTTCCTTCCCCTTCCCCTTCTTCCTTCCTTCCTTCCTTCCCTCCATCCCTCCCTCCTGTACTCCTCTTCTTTCTCAATTCTAAGGTGGCCTTAATTTCTAAGGGACATGGCAAAAGACAGTCTAGTTGGATGAGTGCAGTCACTAATATTATTTCCATGTATGGAAAATAACTGTTTCCTTAGTAACAATTGCATCAAATCAGTTCACCTGCTGCCCAATAGCAATCACAGGATGCATTGGGACAAATAAATATACTGACTGCCCCACAGCCACATGGTCTAAGTCAGTTACTGGAGAGCTGACTGAAGTTTGGGAAGCATATTCATCTTACGACACTGAGACATCCTCGGGGGGTTGCAAACACAGGTAGTGTGAAAATTATCAGAACATCCAAGAAAAGGAAAGTTTGACTAAGTGCCGATAAGATTTATGATGTCATGTCTGACATAGAATTGAAACCATCACAGAGCACATAGAGTGGTATATTTTCCTGTCAAATGAAAATCATTTTCTTTAAAAGTGAAATGAAAGTCTCTAAATACAAATTTACTAGAGGATGTGTAAATTTCCTACTTTTCATTACATACTCTGGACCCAACAGAGGGAAATTGGAGCTGTCAGTGAGCCATACATGCAATCTGGTACAGGATCTATGGATTGAATAGACTTTTTTTCATGGAACTACACAAAGCCAGTCTTAGTCATTTCAAAGAAAATTTGTGGTCATTTCAAAACCACCAGCAATTCCAGGGACACCAAGTTGCATAATTCTAGGGGAAAGTGGACTAAAAGTGAATGGCAGCCTCTGGAGTTATACTGAGCATTATTCTTAAAATGTCAATTTGGCAAATAGGTGGTAAGCGAGATCTGTCTGCCAGATTGTTCACATCATCTCTGCTTTAAAAAGATTGATCATAGAATATGTTAAAATAAGACCTGTGGAGAGGAGGTATGAGCTATTTAAGGTGGAAAGGTGTGGGAGAGGGTGAAATTAGTTTTAAATTTTCTAAAGCACTTTTTAACAGGAAAAGAAGTTCTTGGGCACTGAAGGCAGAATTAGATTAAAAGTATTCAATACTCTTCCATTATCAGAGAAATAGTAAAGCTACTAGAGTGCTTTCTGGTTGGGAAGGAAGAAGGCTACCAACATCTTCAAATAAAGATGAGAAGTAGAGCATATTCTTGGTGGATGAATGGATTCACGTGTACTTTTACTATATGCAAATGAAAAGAGCTTTAAAGATCAATAGTTTAGCAAGACTCTTATTTGAATAAGTGATCTTGGAGTGTTTACCCATTGTAACAAAACTCCTTTTCTCATAATTAGAATCATTAAGAAATTTCTCCTACATTTTAGATGACGGAGGTGTGTGTGCACGTGTGTGTGTATGTGTGTGTGATCAACTCATTGCATATTTCTTAAACCACAATATTGTTTAAGGAATTTTAGAAAATAATTACTCAATAGGAAAATGTTGGCCAATCCTCAGATATTTAGATAAGGCTGATTCAAATGCCTATTCTTTCACTGTTCTCACTATGACACTCTTATTTTTATTATTGGCCTGACTTCCGCAGTTATTTTGAAGTTACAGATTTTTAAATTTTGAGTTGAAAAAAAAGAGCAAATTTAGATTAAGGAATGAGAAGTAGTCCTCGCAGCCTCATGAATCTCCTGAAATTTCGAACGGCAAAATCTAAAATCTACAAGTTATTACCTTCTTACAGTAAATAGGTGGGTGTTATGGGTCGTTTTCTTTAACTTCTTTACTTGAAAAGGAATTAAATGATTTCCCTTTAACATAACTTCCCTTTGATTGTGCTCTGCTTCATGAAGTCTGATTTTATTTGCAATATAATTTACTTCAACTATTCACTGTACCCCATGAAGATTCAGCAGCATTTATAACTATTGTCCATAGTTTCAAAAACTAGGTTGTCTTTCTCTTCTCACCAAGTTTGGGATTAACTATGAAGAACCAAAGTGAACCCTTTCAACAACAAGGTTTGCTGTGGTTTTCAAGTTTTGCCTTTGTGTGGAACATTGTAATGACATAGTGGGAAAAGAAATATTTGGGGAGAGAATTAACCATGGCTGATACATAGCACGGGTATTTCTGAACAACCTACTAAATTATTTCTTAGAACATTTTGAAGTATATCTTGCCATAGGAGTGGGAACAGTTTCATACAAAAGCCTCCTCATGCTTCCAACTTTTCTTTAAAAAATTTTTTTTAAATTATTTTATTAAAAATAGAGGCCCGGCCCGATGGCTCACACTGGTAATCCCAGCACTTTAGGAGGCTGAGGTGGGCAAATCACTTGAGGCCAGGAGTTTGAGAACAGCCTGGCCAACATGGTGAAACCTCATCTCTACTAAAAATCCAAAAATTAACCAGGCCCGGTGGCTCATGCCTGTAATCCCAGCATTTTAAGAGGCTGAGGCGGGTGGATCACTTGAGCCCAGGAGATAGCGACCACCCTGGGCAACATGGCCAAACTTCATCTCTACAAGAAATACAAGTTAGCCTGGCGTGGTGGCACGCACCTGTGGTCCCAGCTACTCAGGAAGCTGAGGTGGGAGGATCACTTGAGTTCGAGGGTGCAGTGAACCAAGATCGCACCACTGCACTCCTTTGGCCTGGGACACAGAACAAGACCCTGTCTCAAAAAACAAAACAAAACAAAACAACCCCCCCCCCGCCCCACACACACACAAATAGTGGAACTATAGCACACAAGAGCCATGCATGAGTCAGTGTTCTCCACGAAAGCAAGCTTCAAAGTGGAACTGAGAGACCGGGCTCTGATCCTCACCCTCCCACTAATACCAGGGTAGCCTCAGCCAAGTCACTTAAATTCATGTCTTGAGAGAAGACAGAATTAACTAAGGAATCCCCAAACAGCAGTTCCCTTAGTTGAAATCACAAACGTACACACACATACCCTGACTAAGGAGAGCTTCAAGAAAGGCTGGAAGACTTAGGAGAGGTCAATGGTGACAATCTTAATTCAGAGTTAAGGTTGTCTCTCTGTCAACTTTGCTCAACGTTGGAGCATCTGTTGTTCTCTTGCAATCCACATTCGTTTTCGAAAACACTTCAGAAACAAAAATAGCATCAGCGGTGGTTGCCTGTGAACCTAAAACATATCATTCCAATGAATAAAATCAAGCAACCCTCCAGTTAACCTCATATCCAATTTTTCCTTTCCAGAAAGAAGAGATTTTATCGACATCGAAAGTAAATTTGCCCTAAGGACCCCTGAAGACACAGCTGAGGACACTTGCCACCTCATTCCCGGAGTAGCAGAGTCCGTGGCTACCTGTCATTTCAATCACAGCAGCAAAACCTTCATGGTGATCCATGGCTGGACGGTAAGGGAGGCTCTTTGGGGAAGAGTGGATTGGGGTGGTGAGGTATCCTGACTGGCCTGCCCAATTGTTGGGGACCCAGTGATGGGTCCGCACCCCACATCTCACGTGGATCTCCTTACACTTGAATAAAGACAGTTCTGGCTCAGGTGGGATCTGAAGCCACAGGTTCATGAGAACTCCCCCTAGGCAGTGCCAGCCTTCATTTTAACACTGTACCTGGTTGGTGCCCTTGAGCCAGAGCTTCCTGCGAGGTTGGTAAAGGATGCTCTGCCCAGCTACTGAGCAGAAGATAGGTGATTGCTGTGGGGAACCGGTGGAACCCTGGCATGATCCCGCATCACCCAGCACATTGCCAGGAGAACCTTTCTAAAGAAGACAGCATGGAAGAGTGAGGAGAGGGCTGGAGTGGAGTGAGGAAGTGTGGCGTCCATGCTGGCTTTGCCATTCTCCAGCTCTGTGCTGTGGATCAAGTCACTTGTCATCTCTGGGCCTCCATTTACTGATCTGTAAAGCAGAGGTTGCACTAGATGTCCCTAAAAACACTCTACTTCCAAAATTTTCCAGTTGTAAACTTTAGGGAGCCTTTCTGGAAATTAAAAAAAAAAAGGGCTGGGCAGGGAACTGACATGCTGACATGCCAGATGATTAGAAAAAGTGAAACTGTGATTAAATTTACTTTAAAAAATTGTTAAAAGTCCCCTCTCTAATATGTCACACAGTCACTTTAGATTATGTAGTTCTTGCTTAGTTTGTTTTAAAATGTTTCAATCCCAAGTGAGTAGACTGTGTAATTTATTTAAAAGGATTTGTGTAAAATGGCTTGTTAAAGATATATTATCTATCCATTATGTCAGAGCTTGTGAATATTATAATACTAATTTCTAGTGAGAACTAACTAAAAATAACATTCATCTAGTCCACATTTCTTTTCCTACATAATTTAAGTGGCTTTGTTATTGACTGAAGAGGACAATCGCTACAATTTTTATAGAGATGAGACTACTACTAACAGAATTCACACAGATGTTTAACAAAATATAGCCATTTACTATGTTAACATAGTAAATTCAATATTTTGTGATAAAATCTCAAATTCCTAAACATAAATCCTTTAATATTTTAATAGGTGTAAGTAGGAAAAAGGAATCTTTTTCTTTTTTGAGATGGAGGCTCACTCTATTGCCCAGGCTGGAGTGCAGTGGCGCGATCTCGGCTCACTGCAACCTCCGCCACCTGGGCATAAGAGATTCTCCTGCCTCAGCCTCCCGAGTAGCTGGGACTACAGGCGCCCACCACCACACCCGGCCAATTTTTATATTTTCAGCACAGGCAGGGTTTCACCCTGTTGGCCAGGCTGGTCTCGAACTCCTGACCTCAAGTGATCTACCCACCTTGGCCTCCCAAAGTGTTGGGATGACAGGCGAGAGCCACTGCATCTGGCCCAGAAAAAGGAATCTTTAGAGGCCCTTTGGTTATATACAGAACTTGGATTTTAAAATTTTTCAAAAAATCTGAGCTTAATAAACGCATTTATACAACAGAAATAAATTGAGTATCTCAGTCATCTCAATCTTATCCCTGAGAGAATTTTATACTTTGGGAAGTTGTGGGAAAAAAATGGTTCTTTTTCATACTAAGATGACATGACCAACCCAATATCAACAGACGGTGCCACTTCCTATCATTGGTCCTACTGCCTCACTAAGCCCACCTGTATCTTTCACATCATGTGTCCTGACATTTTGAGTGCTTGAGCACAGAGACTGCTGTCTGGCTGTGGGACTGAGTTGGGTCTGTGCAAGAACTAAGCCAGCCACACTGATCTTGATTATCTCAGTGAACTCACTGGCAGGGTCAGGTGGCCCACCTGGTATAGGCAGCAGGGAGGGCTTCAGTTCAGCTGCGTGTCTGAAAACCAAAGATTTAAAACATAGTAATTATTGAACCTCAGAAGAAAAACTCAGATTGAAAGAACTTAGAATAAGACCCTTTTTGAGTTGAGAAAGGTGAGTACTTAGATTTTTCATTTGCTTTGTTTGGGATTACTTACATCAGTATTTTATGTTGATCAGAAAGAAAGGATTCAATTAGCTATTGTTCGGTTAATAAAAATGTCAGCCACTGTAGGAGTAAGTTGGATGTCCAGCCTTTTTAGATTGCTTAACTTGGAAACACTGGGCTGGGAGCGGTGGCTCATGCCTGTGATCCCAGCACTCTGGGAGGCCAAGGCAGGCAGATCACTGGAGGTCAGGAGTTTGAGACCAACCTGGCCAACATGGGGAAACCCAATCTCTACTAAAAAAATACAAAAAAATTAGCCAGGTGTGGTGGTATGTGCTTGTAGTCCCAGCTACTCAGGAGGCTGAGGCAGGAGAATCGCTTGAACCAAGGAGGCGGAGGTTGCACTGAGCTGAGATCATGCCACTGCACTCCAGCCTGGGCGAGACAGTAAGACTCTGCCAAAAGAAAGGAAAGAAAGGAGGGAAAGAAAGGAAGGAATGAAAGGAAGGAAGGGAAGGAGGAAGGGAAGGAGGGAGGGAGGAAGGAAGGAAGGAATGAAGGAAGGAAGGAAGGAATGAAGGAAGGAAGGAAGGGAGGGAGGAAGAAAGGAAGGAAGAACAAAGAAAAGAGAAACACTGGTAGTACAGAAAAACTTCTGATAGAGGCCTAGAGTAAACCCGATTTTCTTGCCTTATCTGAAATAAGCTGCCTGGGGACTCACAGGCACAGACGAAGGGAAATGAGGAGGCTCTCCAGCTGTGTCATGAGACACCCAAAGGAATGCTTAGCATGTAGCATGCATGTGATACATCCCAGCAGGTTGCTTAGACACAGCTATCTTGGAGCTTTGCCACTTGCTTGGATGTCACTGGCTTTAAGTACAGGGTTCCCATTGTGAAGTAGGGGATCCTGGCTGAAACAGGGAGACATTAACATTACATTCTGAAGAAATGACATCAACCTCTCCTGATCTTGAAAGCCAACTACAAAGGGTGCCCAACACCCCAACCTTGAAGGGAGGCGAAGGTGAGTGGGACTGGACCAATTCAACAGGGTTCTGCTCCTAGCCAGGTGCTCCTGCTAGTTTCCTCAAAGACCCACTTTGCATTCAGACCAATCTTTCCTTTTAATAGTATAAATGATCAAAATTTTATTGAATGTCTAAAATATACTTTTTAAATGGGAAGCATGGTGAACCCCAATCTGCCGTTCCTCAACTCAACTCAATGCCTTCCTGGCTTACTTAGATCTGCCTTGGAAGGGACAGACCTGTCTCTGAACACTGTTCTGTTATTTGATTTTTCTATCTGTGCCAATGGGTTTCCAATCAAGTTTGTTTTTTCCATTTCATGCAGGTGTATTGGGCTGATGTATCTATGACAAGTGGTAGGTGGGTATTTTAAGAAAGCTTGTGTCATCATCTTCAGGTAACAGGAATGTATGAGAGTTGGGTGCCAAAACTTGTGGCCGCCCTGTACAAGAGAGAACCAGACTCCAATGTCATTGTGGTGGACTGGCTGTCACGGGCTCAGGAGCATTACCCAGTGTCCGCGGGCTACACCAAACTGGTGGGACAGGATGTGGCCCGGTTTATCAACTGGATGGAGGTAAGACTGGGAGAAGGAGACTTATGTGTCCAAAACAGTGTTTTTGACTGGAGCCAGAAAACCGGCTGTTCTTTCTTCCTTTTCTCTTAGATTTAAATATTTTCTGGGGGCATTCAAATCTTCAGAATCAGCGTGGATATTATTTTATATCCAAAAGCAACATTTTGATAAGAATAGACTATAAGGCCAATAAATAGTCCTGCCCTGCTCTATCGTTTGATATTTTCACAGTGAATAGATCTGCTGAGACCAATAACTAAGTGGGCCCAACAGAAAAAAGCTTGTGATTTTCGGACAGAGGAAAGATGGCATGTTCAGCCAGACCCTTCCCTACCAGTTGGCTGGCCTGTGGACATCTTATCCTCACCTTGACATACCAATCTCTTTCATGAAAATATTAATAGTACTTATTCTTTAGTGTGAAATAGGATGAACGTTTTTGTTGAGCATTGGGAGAGTGATGGAATTGAGCTAGGAAGATGTTGGAAGGGAAGGTGCATAGGATAGAAGGGAACTGAGGTCTGGAGTTCTGACTTAGCTGCAAGAGACCCACTTTTTCACACGATCCCTTGAGAAGTACCTCTGAAAAGTATCTTGGGGTTGGAAAGAAGCTGATACTCTGACCAAGGCAAATTATTTTAACCAGGTAATTGGAAGTAAAAAATAAGCTGTGTTTATTAGACTGATCATAAAAGACAAAAGTTCTTTTCTTGTCTTTTTTGCTGACCAGGCAAATGAACATGGGCAAACGGGATCACCTCCCTGGGGCTCAGGCTTCTCACCTGTTAAATGAGGGGCTGGACCATGTACCTCTGGTCCCTTCCACTGAATGTTTCCTGAGTCTGTCATTGCTTGGCTAACCTTCAATGATAAAGTGATACAGATATTTAGAGTAAGGAATAATGGGAAAATATATACCCATATACTATACATTCAAACATACACACATATACATATATGCATGCATATAAATGTATACGCATATGTATATGTGTATATGTTTGTACGTATAGTATATGGTTATATATGCAAATACATATACATATACAAACATATGCATATATTATATACATAAATAATACTATTTCAGATGCATGGAAAAACTTTGTAATTTAAATCTGCTATTAAAGAAAGAGAAAATCAATTCTGGATTTGTTTACGGAAAAGTGAAACAAAAGAAAAAGACAATTTTAACACTAGAGAATATTTTCTCTCTCTTACCTGTAACACAAAATTAAAATAAGTAGAATTAGTTTTCAGTATTTCCTATATTTGGAAAACAATATTTATATTCATTTTGTTTCTTTTAGTTTTATTTTTGGCAGAACTGTAAGCACCTTCATTTTCTTTTTCTTCCAAAGGAGGAGTTTAACTACCCTCTGGACAATGTCCATCTCTTGGGATACAGCCTTGGAGCCCATGCTGCTGGCATTGCAGGAAGTCTGACCAATAAGAAAGTCAACAGAATTACTGGTAAGAAAGCAATTTCGTTGGTCTTATCATAAGAGGTGAAAAGACTGTCATTCTGAGAGAGAATCAGAACAAATTTTGTTAAATACCCACATGTGTGGTGTTCTTCCCGGAGACATGACCAGCACTTGATTATCTCATTGTAGGGCTCTTTATTAGGGATAAGAAAAAACACAGACGCTCTCACTGGCTTACTATCCACTGGCAATAGCACAGAAATAAAGCATAATTACACACAATGCCTGCAGATTTCTCTGGGAAGCCTGTTTCCTCCCACTCTCAGCTCTGTGTTTTAGTAGTGTAAATGCACATCAGTACTAGGAGAAAAGAAGAAGGACCAATTCCAGAGGCCACTTCGAAAGAAGACCGTCATCTAGGCAAAGGTGTGGCATACACACAGAGAGAAAGAACCCACCACTGTTTATACATCTTCTCGACATATTCAGAAATAATCTACAAAAGGAAATCCAGCCATCCTGAGTGGAAACTGCTGCATAAGGCTAGTTTAAGAGACTCAAATTCATTTTAGAAGGAGCCAAGCCTCCTTTTATGTCTCTCTAAGTAAAGATACCATGACTGTAGAATAGGAGCTAATAAGAATCTAAATAGCTGCCAGTGCATTCAAATGATGAGCAGTGACATGCGAATGTCATACGAATGGAAATTTACAAATCTGTGTTCCTGCTTTTTTCCCTTTTAAGGCCTCGATCCAGCTGGACCTAACTTTGAGTATGCAGAAGCCCCGAGTCGTCTTTCTCCTGATGATGCAGATTTTGTAGACGTCTTACACACATTCACCAGAGGGTCCCCTGGTCGAAGCATTGGAATCCAGAAACCAGTTGGGCATGTTGACATTTACCCGAATGGAGGTACTTTTCAGCCAGGATGTAACATTGGAGAAGCTATCCGCGTGATTGCAGAGAGAGGACTTGGAGGTAAATATTATTTAGAAGCGAATTAAATGTGACTCTTATCCTTAACCCTTATTGACCCAATGTCCTACTCAGTAGCTTCAAAGTATGTAGTTTTCATATACACATTTGGCCAAATTATGTTTCTGAAGAATTCTGCAATGTTCAGCATGACCACCTTAGAGCCAGGCAGACAGCCATTTTATCTTTTATTTACTATACTGTAGGCTACACTGAGCAGTGCACTTACAGTAGCAAGAGAAAAAGGTGGGATTTTAGACAGGAAGACTCCACTGACCTCAATAATGGCATCATAAAATGCTATCTGGCCACATGTTGTCATACCTTGAATGTAGCTGCAAAGCCAATGGAAAGATTTTAGATGTTACTGGAACAGAAGATGTTAATTAGGATAAATCTTCCAAAATGTTCAGAACATAATGTTAGCTTAATGTTTTACTTTAATAATGTTAGCTTGTGTTAAATTTATGATTTTTGTTTGTTTGTTTTTTGAGATAGAGTCTTATTCTATTGCCCAAGCTGGGGTGCAGTCACACAATCACAGGGACTTGCAATGTTGCCCAGGCTGGTCTCAAACTCCTGGCCTCAAGTGATCCTCCTGCCTCAGCCTCCCAAAGTTCTGGGATTGCAGCTGTGAGCCACCACGCCCAGTTTACGATTTATTTTTAAGAGCCCCTTGCATACTTTATAGACATTGGGACCTACCTAGGATATTCTCGTTATTTTTGTGCACGTAATAGAACTTAGAGCATATTGTTACTATTTTCGATTGTCCTAAAAACTTACAAGGAATTCATTCTTATGGCATTGCTGATTATTTCTATGTTCATTTGATATAAAAGAGTGTTAGTAGGGGCAGAACCCTCAATTGTACATAATATCAATGATAAAATACAATTCATTTAACAATTACCCTCTTAAGATGTGGTTTCTAGAAATACAAATTGTCCCTAACTTACAGTTTTCCAACTTTACAATTGGGCTGTAACACCATTTTAAGTTGAGAAGCACGTGATGGTTTGACTTAAAACTTTTTGACATTATGATGGGTTTTGGGGGTATTAAGTGCATTTTGACTTACAGTATTTTTGACTTATGAAGAATTTATTGTAAGGCAAGGGGCAGGTATATGTTTCTAGAAGCACCTAGAAGTGTTAGACACTTTCAATGTAAGAGAAGGATGAGATAAACAAGGAAATCAACCTCCACCTTGGAGGCTTATTACAGCTTCATAAACATACTCATAAATATAAGAAGCACAAAAGTCAAAAATTCCCTGTGAACTTGCAACTTTCACTCTCTTGAAGGTGGGTGGGCCGCTACCACCAAGAATATCTCCTGAAATAGGGCCTACAATCATAAATGCACAGGACTATATCCTTGGGTGATTCTACTCTAACACCACATCTCACCTATTTTAGACATGCCAAATGAAACACTCTTTGTGAATTTCTGCCGAGATACAATCTTGGTGTCTCTTTTTTACCCAGATGTGGACCAGCTAGTGAAGTGCTCCCACGAGCGCTCCATTCATCTCTTCATCGACTCTCTGTTGAATGAAGAAAATCCAAGTAAGGCCTACAGGTGCAGTTCCAAGGAAGCCTTTGAGAAAGGGCTCTGCTTGAGTTGTAGAAAGAACCGCTGCAACAATCTGGGCTATGAGATCAATAAAGTCAGAGCCAAAAGAAGCAGCAAAATGTACCTGAAGACTCGTTCTCAGATGCCCTACAAAGGTAGGCTGGAGACTGTTGTAAATAAGGAAACCAAGGAGTCCTATTTCATCATGCTCACTGCATCACATGTACTGATTCTGTCCATTGGAACAGAGATGATGACTGGTGTTACTAAACCCTGAGCCCTGGTGTTTCTGTTGATAGGGGGTTGCATTGATCCATTTGTCTGAGGCTTCTAATTCCCATTGTCAGCAAGGTCCCAGTGCTCAGTGTGGGATTTGCAGCCTTGCTCGCTGCCCTCCCCTGTAAATGTGGCCATTAGCATGGGCTAGGCTATCAGCACAGAGCTCAGAGCTCATTTGGAACCATCCACCTCGGGTCAACAAACTATAACCCTTGTGCCAAATCCAGCCTACTTCCTGCTTTTGTAAATAGTTTTTTTAAAACTTTTAAGTTCAGGGGTACGTATGTAGGTTTGCTAAAAAGGTAAACTTGTGACATGGGAGTTTGTTGTCCAGAATATTCCATCACCCAGGTATTAAGCTTAGTACCCATTAGTTACTTTTCCTGAAGCTCTCCCTCCTCCCACCCTCTGGGAGGCCCCAGTGTCTGTTGTTCCCCTCTATGTGCTCATGCAAAGTTTTATTAGGACACAGCCACACACATTCATTACCATATTGTCAAAGGCTGGTTTCATGCCACCATAACAGAGTTGATAGCCCACAGAGCCTAAAATATTTACTCCCTGGCCCTTTACAGAATGTTCACAACTTACATAAAGGCAAGGACCATCTGTCTTATTTATTTATTTATTTAATTTGAGATGAAGTCTAGCTTTCTCCTAGGCTGGAGGAGAGGGGCATGATCTTGGCTCACCACAACCTCTGCCTCCCGGGTTCAAATGATTCCCCTGCCTCAGCCTCCGGAGTAGCTGGGATAACAGGCATGCACCATCATGCCCAGCTAATTTTTGTATTTTTAGTAGAGAGGGGGTTTCACCGTGTTGACCAGGCTGGTCTCGAACTGCTGACCTCAGGTGATCTGCCCTCCTTGGCCTCATCTGTCTTTTTAAATGCAACTATTCCTGGAAGGCAAGAATATCTCACACCTTCTAAGATACTGCCATTTTGCCAGGAGTTTGTTTCACACTTGAATTTCAAGCTTGGCCTCTTGTTTAGAGGCAGACCTAAAGGAATGGTCGGAAAATGAGAGAGGAGGTCTTCGGATAAATCCGGTGAGAGGGACCAACTTCAGGAAGGGTGGCTTTTGTGGAATCCAGATGGAAACCTGAGGGAAGGGATGATATTAAAGAACAGTGGCCCCAGGTAAAACATATGGCACCCATGTGTAAGGTGATTCTTAGAATCTGTAGAGGTGTCTTTCGTGGTATAGAGGTTGAGGCACCTGTGCTTCAAGGAAACCTTAACTCTTCAAAATCAGGCAATGCGTATGAGGTAAAGAGAGGACTGTGGGACCATAATCTTGAAGACACAGACAGGCTTCACTCATCCCTGCCTCCTGCACCAGTGGGTTCAAGGCTCTGTCAGTGTCCCCTAGGGGCACCTCACCACTCCCAGCTTCTTCAGCTCTGGCCTGTCCTGCTGCCTGCAAGGGTTTTGCTTAATTCTCAATTCAATGTCTCTTCATCTTTTAGCAGCTGTGGGGTTTTGTTGTTGTTCTTCTGTTTTTGCTTAGTATCTGACTACTTTTTAATTATAAAAAGAGATGTATCTAAACAAAATAGAGATTGTTATCAGAAGTTCACAACATTTATTAAAAATTTTTTCACCTGGACAAGAGTCTAAAGCAGCATAAAAATATGGTCTGCTATATTCTAAACCATCAGTCTTAAGAGATCTGTGTCTCAGCTTAAGAGAAAATACATTTAATAGACAGTAACACAAATAAGAAAAAAATCTGACCAAGGATAGTGGGATATAGAAGAAAAAACATTCCAAGAATTATTTTATTTATTTATTTATTTATTTATTTATTTATTTATTTATTTATTTTTGAGACACAGTCTCGCTCAGTTACCCAGGCTGGAGTGCAGCGGCGCAATCTTAGCTCACTGCAACCTCTGCTTTCCGGTTCAAGCGATTCTCCTGCCTCAGCCTCCTGAGTAACTGGGATTACAGGCACCCGCCACCACGCCCAACTAATTTCTGTATTTTTCTTAGTAGAAACAGGGTTTCACCATGTTGGCCAAGCTAGTCTCAAACTCCTGACCTCAGGTGATTCACCCACCAAGGCCTCCCAAAGTGCTGGGATTACAGGCATGAGCCACCATGCCTGGCCTCCAAGAACTCTTTTTTCCTCCATCATCATGGTTCTATTTTAGTCCTGCTGCCTTTCCTTTTAACCTCTCCCCAGGCCCATTTGCTCAGGGTTTTTGGTAGAGACCAGAGGAGGGGCAGGGAGGAGATATAGAAGTTCAACTACCTGCTTCCAGAGGCTGTCCCTAGTATAGAATACTTTAGGGGCTGGCTTTACAAGGCAGTCCTTGTGGCCTCACTGATGGCTCAATGAAATAAGTTCTTTTTTAAAAAAAATTTTATTTATTTCCATAGGTTATTGGGGGAACAGGTGGTGTTTGGTTACATGAGTAAGTTCTTTAGTAGTGATTTGTGAGATTTTGGTGTGCCCATTACGGAATGGAAAAATCAACGAAATAAGTTCTATGATGCACCTACTAGACACCTAATCTGCGCTAGATGGTGGGGGAATTAAGAGCATGGGCATGATCCTGTGACCGGAAGCCCGCTTACAGTCAGGGTGGAGGACAGACCTACTCATGAAACAAACACAGTGACATATAGTGACACAGAAGCAAATGTCAAATATGCTTGCTCCAGATGCTAAGGCACAAGATGGCCAAGGATGGCGGAGTTCATGGAGAAAGCATCATGAGTGTTTTGGCCTTCTGATTTGATCTCCCTAGCACCCCTCAAAGATGGCTACTTCCTAATGCTGCTTGGCAATTCAGACACATTTGGGTTTTTCCTATGCATATAACCACACTTTTCTGAAAGGGAGTAGAATTCAAGGTCTGCATTTTCTAGGTATGAACACTGTGCATGATGAAGTCTTTCCAAGCCACACCAGTGGTTCCATGTGTGTGCACTTCCGGTTTGAGTGCTAGTGAGATACTTCTGTGGTTCTGAATTGCCTGACTATTTGGGGTTGTGATATTTTCATAAAGATTGATCAACATGTTCGAATTTCCTCCCCAACAGTCTTCCATTACCAAGTAAAGATTCATTTTTCTGGGACTGAGAGTGAAACCCATACCAATCAGGCCTTTGAGATTTCTCTGTATGGCACCGTGGCCGAGAGTGAGAACATCCCATTCACTCTGTGAGTAGCACAGGGGGGCGGTCATCATGGCACCAGTCCCTCTCCTGCCATAACCCTTGGTCTGAGCAGCAGAAGCAGAGAGCGATGCCTAGAAAACAAGTCTTTAGTTAAAAAAATCAGAATTTCAAAATTGAGGTCTTTCCTCTATTTGATATTGAGAAAAAAATGCTTCAAATTGGCCATTTTATTTTCACTTACTAGTTATATTTTTTTATTTATCATCTTATATCTGTTTATTTCTTTTATAAAGCTGCTGTTAAACAATATAATTAAACTATCTCAAAAGGTTTGACATTAAAGAAAATGAGCAATGGTAACAGGAAACCACTCTATAGATGTACATATAATATGTACAGAAAATATAAGTAGTAAGAAGTCCATGACAAAGTGTTAGCTCTTTTTTTTTTTTTTTTTTTTTTTTTTTTTGAGATGGAGTCTCTCTCTATTGCCCAGGCTGGAGTGCAGTGATTCGATCTCAGCTCACTGCAACCTCTACCTCCCGAGTTCAAACAATTCTTCTGTCTCAGCCTCCCGAGTAGCTGGGGCTGCAGGTGCCCACCACCATGCCCAGCTAATTTTTGTATTTTTAGTAGCGACAGGGTCTCACCATGTTGGCCAAGCTGGTCTTGAATTCCTGATCTCAGGTGATCCACCCGCCTCGGCCTCCCAAAGTGCTGGGATTACAGGTGTGAGCCACCATGCCCAGCCTACCCTTTACTACTAATCAAAGAAATAAAAGTAAGGCAACTTGATACTTTTACAATTACTAGATGAACAAATCTTTAAAAATAGCCAGTGCAGACAAGGTGGTGAAGCAGAACATGCGAACCTACCATGCATCATTCACGGCTAGAACCCTCCAGGTGCGGAAGGTAGTATTTTAATAACTTTCCATAGCTACAAAATATTATTACATAGAAGGGAGTGATTTTTTTCTAATATTTATCCTAAAGAAATAGTCAACAAACATTTTTAAAAACATCAATTACAGTCGTACCTATACTAGCATAAATTAGAAACCCAGTATCCAACATTGAGGCAGTGGGTAAATGAATCGTGGTTTATCAAGTCATTAAAATCAATCTAGCCTTTAAAAACTATAATTGTAGGAAACCCAGGAAAACATAGTAAAAAATGGAATATAAAATCTGAAGAGAATAAAGAATAGAGAATCGTATGTGTGCTATGATTGTAGCTAAATAATGTTCAAGTATCAACACAAATTGAAAAGGAATACATGAAAATGAAAATTATATTTCTGAATGATTGACTTCAGGATTTTCTTTTAGAATTGTATTAAATAGTTCATGTCATTAGGATAAATGCTGGAATGTGGATATAATTTAAAATATACTAAATGCCATCGACCTTCATTTTGAGTTCTTTGTTGGACATTTTTGTGCATTTTTAAAATATCCCCTAAATAATAAAGCTATTTATATTTGGAGAGGAGAAAAAAAAGTGGGGGGCAGGGAGAGCTGATCTCTATAACTAACCAAATTTATTGCTTTTTTGTTTAGGCCTGAAGTTTCCACAAATAAGACCTACTCCTTCCTAATTTACACAGAGGTAGATATTGGAGAACTACTCATGTTGAAGCTCAAATGGAAGAGTGATTCATACTTTAGCTGGTCAGACTGGTGGAGCAGTCCCGGCTTCGCCATTCAGAAGATCAGAGTAAAAGCAGGAGAGACTCAGAAAAAGTAATTAAATGTATTTTTCTTCCTTCACTTTAGACCCCCACCTGATGTCAGGACCTAGGGGCTGTATTTCAGGGGCCTTCACAATTCAGGGAGAGCTTTAGGAAACCTTGTATTTATTACTGTATGATGTAGATTTTCTTTAGGAGTCTTCTTTTATTTTCTTATTTTTGGGGGGCAGGGGGGGGGAAGTGACAGTATTTTTGTATTTCATGTAAGGAAAACATAAGCCCTGAATCGCTCACAGTTATTCAGTGAGAGCTGGGATTAGAAGTCAGGAATCTCAGCTTCTCATTTGGCACTGTTTCTTGTAAGTACAAAATAGTTAGGGAACAAACCTCCGAGATGCTACCTGGATAATCAAAGATTCAAACCAACCTCTTCAAGAAGGGTGAGATTCCAAGATAATCTCAACCTGTCTCCGCAGCCCCACCCATGTGTACCCATAAAATGAATTACACAGAGATCGCTATAGGATTTAAAGCTTTTATACTAAATGTGCTGGGATTTTGCAAACTATAGTGTGCTGTTATTGTTAATTTAAAAAAACTCTAAGTTAGGATTGACAAATTATTTCTCTTTAGTCATTTGCTTGTATCACCAAAGAAGCAAACAAACAAACAAAAAAAAAAAGAAAAAGATCTTGGGGATGGAAATGTTATAAAGAATCTTTTTTACACTAGCAATGTCTAGCTGAAGGCAGATGCCCTAATTCCTTAATGCAGATGCTAAGAGATGGCAGAGTTGATCTTTTATCATCTCTTGGTGAAAGCCCAGTAACATAAGACTGCTCTAGGCTGTCTGCATGCCTGTCTATCTAAATTAACTAGCTTGGTTGCTGAACACCAGGTTAGGCTCTCAAATTACCCTCTGATTCTGATGTGGCCTGAGTGTGACAGTTAATTATTGGGAATATCAAAACAATTACCCAGCATGATCATGTATTATTTAAACAGTCCTGACAGAACTGTACCTTTGTGAACAGTGCTTTTGATTGTTCTACATGGCATATTCACATCCATTTTCTTCCACAGGGTGATCTTCTGTTCTAGGGAGAAAGTGTCTCATTTGCAGAAAGGAAAGGCACCTGCGGTATTTGTGAAATGCCATGACAAGTCTCTGAATAAGAAGTCAGGCTGGTGAGCATTCTGGGCTAAAGCTGACTGGGCATCCTGAGCTTGCACCCTAAGGGAGGCAGCTTCATGCATTCCTCTTCACCCCATCACCAGCAGCTTGCCCTGACTCATGTGATCAAAGCATTCAATCAGTCTTTCTTAGTCCTTCTGCATATGTATCAAATGGGTCTGTTGCTTTATGCAATACTTCCTCTTTTTTTCTTTCTCCTCTTGTTTCTCCCAGCCCGGACCTTCAACCCAGGCACACATTTTAGGTTTTATTTTACTCCTTGAACTACCCCTGAATCTTCACTTCTCCTTTTTTCTCTACTGCGTCTCTGCTGACTTTGCAGATGCCATCTGCAGAGCATGTAACACAAGTTTAGTAGTTGCCGTTCTGGCTGTGGGTGCAGCTCTTCCCAGGATGTATTCAGGGAAGTAAAAAGATCTCACTGCATCACCTGCAGCCACATAGTTCTTGATTCTCCAAGTGCCAGCATACTCCGGGACACACAGCCAACAGGGCTGCCCCAAGCACCCATCTCAAAACCCTCAAAGCTGCCAAGCAAACAGAATGAGAGTTATAGGAAACTGTTCTCTCTTCTATCTCCAAACAACTCTGTGCCTCTTTCCTACCTGACCTTTAGGGCTAATCCATGTGGCAGCTGTTAGCTGCATCTTTCCAGAGCGTCAGTACTGAGAGGACACTAAGCATGTGACCTTCACTACTCCTGTTCTGAATTCCAGGAATATGCCCTTTTCAACCCTCCACACATCCCCTGCCAGACAGCAAGTGCTAATGGGTTACAGGAACAAAGGGGAGAAATATTAGATCATGTCATACAAGCCAGTGACACAAGAAATGAAGGGAAAGGCTAGACACAGTGTCATCTGGAAACAGGAAAAGCAATTGCTTTTGGTTTGTTCTTTTCCTAGTTTGCATTTGGGACAAATGTATAGAATAAGAATTGCCTTCATGCCTGCAATCCCAGCACTTTGGGAGGCTGAGGCAGGTGGATCACCTGAGGTCAGGAGTTTGAGACCAGCCTGGCCAACGTGGCGAAACCACCTCTCTACTAAAAATATAAAAATTAGCTGGGTGTGGCGGCACATGCCTGTAATCCCAGCTACTCGGCAGGCTGAGGCGGGAGAATTGCTTGAACCGGGGAGGCAGAGGTTGCAGTGAGATGAGATCGCGCCATTATATTCCAGCCTGGGCAACAGAGAAAGACTCCATCTCAAAAAAAAAAAAAACATGCCTATTAGGAAAAGTATATTAAAGACCCTATGTGTAACATCTTTAATGTTTTTAAATTCTACTTTATAATAGATTTTATACATGTTTACTATAAATAGATTAGGAAAAATAAGCAAAAATAAAATAAAATCACTGTGACCATATCACTCAGAGACAACCCCAATTAACGTTTTTATTTATATTCTTTCGGACTTTATATATACATAATATTTATATGTTTTTCGTCCTTTACAAAAATAGAATTATGGTGTATATACTCTGAATGACTAGATGAGAACATCTGGATCAAAAGCATTAATGTAAGAGCATTCAGGATAAACTCAAAATGGAGAATAGTTAGTGGTATTGAGCCAGGCAAAATAACGCAATTCTTATCTAACTGGAGACTTTTCTTCTAAGAGGTTATTACGTTGTTTTTCCTCATCACAAATCTGAGGCAATATCATACTTTCTTCAGTTCTTAGAAAGAGACTTTTAGATGAAGTTTTTTTTGTTTGTTTTGGTTTTTTTTTTCTTGAGATGGAGTTTTGCTCTTGCTGCCCAGGCTGGAGTGTAGTGGCTCGATCTCAGCTCACTGCAACCTCCACCTCCTGGGTTCAAGCAATTCTCCTGCCTCAGCCTCCCAAGTAGCTGGGATTACACGTGTCCGCCACCACACCTGGCTAATTTCGTATTTTTAGTAGAGAAAGGGTTTCACCATGTTGGTCAGGCTGGTCTTGAACTCCTAACCTCAGGTTATCCACCTGCCTCGGCCTCCCAAAGTGCTGGGATTATAGGTGTGAGTCACCACACCCGGCCCTAGATGCAGTTTTATACATGCATTTGTATTACACATAAATAGCATGCATATTCTGCCAGAGCATCTACAACTTTAAATCTACATGTGAATGTGAAAATAAAACCTCATTAAATTAGTAAATAACTCTAGCTGCTTGTAAAGCACGTCCAGTCGTATTTTTTATATGTTACAAGACTTTATCTGAGAAAGCCTAATGAAGCATTCCTTGTCTGATTATAGGATTACTGACAGAACAGTTATTTAGACAGAGAATGTTCAGATGCGTTTTATTTTTATTTTTTACTTTTATTTATTTTTGAGACAGTCTCGTTCTGTTGCCCAGGCTAGAATGTGGTGGCGTGATCTCGGCTCAATGCAACTCTGCCTCCCGGGTTCAAGTGATTCTTGTGCCTCAGCCTGACAAGTAGCTGGGATTATAGGTGCCCGTTACCATGCCCAGCTAATTTTTCTGTTTTTAGTAGAGACGGAGTTTCACCATATTGGCCAGGCTGGTCATTGAACTCCTAACCTCAGGTGATGTGCCTGTCTCAGCCTCCCAATGTGCTGGGATTACAGGCATGAGCCACAGCACCCAGCCAGATGCATTTTTAAAAACGTACCTGAACTTTATCTAGGAGGTAATTATAAATTAGACTAATAATCTTCTACAGTTTCTTTCTTCTGTGATTAAAATCAATCAAATCAAAGATTCTCTTTCTCACACCTTCTGCTAACTCCTCAGAAACCTCATATCACAAGAAATGAAATGGAACAGGCCTTTCGTTTGATACATTTTAGAATAAGAAATCCTCTAAATTTAGAAGTCATTTGGCCCAGTCCTCCAAAAATGATGCACCTTATTGGGACGGGGCTAAATAGTTGCTCCAGTGTCTTCCATTCCTACAAACCTGCCATTCTCTGATCCATTATACACATCTCCCCTGGGTTTATTCTCACAACCTTTGTTCTGAAATTCCATTTGAAGGCTTTTTCCATCCTAAAACCAGTGGGGGACAGGCGGGAATTGTAAAACACTCAGAAGATAATAAATTGCCCTTTTTCCTGTGCTTTTTCTCAGAAACTGGGCGAATCTACAGAACAAAGAACGGCATGTGAATTCTGTGAAGAATGAAGTGGAGGAAGTAACTTTTACAAAACATACCCAGTGTTTGGGGTGTTTCAAAAGTGGATTTTCCTGAATATTAATCCCAGCCCTACCCTTGTTAGTTATTTTAGGAGACAGTCTCAAGCACTAAAAAGTGGCTAATTCAATTTATGGGGTATAGTGGCCAAATAGCACATCCTCCAACGTTAAAAGACAGTGGATCATGAAAAGTGCTGTTTTGTCCTTTGAGAAAGAAATAATTGTTTGAGCGCAGAGTAAAATAAGGCTCCTTCATGTGGCGTATTGGGCCATAGCCTATAATTGGTTAGAACCTCCTATTTTAATTGGAATTCTGGATCTTTCGGACTGAGGCCTTCTCAAACTTTACTCTAAGTCTCCAAGAATACAGAAAATGCTTTTCCGCGGCACGAATCAGACTCATCTACACAGCAGTATGAATGATGTTTTAGAATGATTCCCTCTTGCTATTGGAATGTGGTCCAGACGTCAACCAGGAACATGTAACTTGGAGAGGGACGAAGAAAGGGTCTGATAAACACAGAGGTTTTAAACAGTCCCTACCATTGGCCTGCATCATGACAAAGTTACAAATTCAAGGAGATATAAAATCTAGATCAATTAATTCTTAATAGGCTTTATCGTTTATTGCTTAATCCCTCTCTCCCCCTTCTTTTTTGTCTCAAGATTATATTATAATAATGTTCTCTGGGTAGGTGTTGAAAATGAGCCTGTAATCCTCAGCTGACACATAATTTGAATGGTGCAGAAAAAAAAAAAGAAACCGTAATTTTATTATTAGATTCTCCAAATGATTTTCATCAATTTAAAATCATTCAATATCTGACAGTTACTCTTCAGTTTTAGGCTTACCTTGGTCATGCTTCAGTTGTACTTCCAGTGCGTCTCTTTTGTTCCTGGCTTTGACATGAAAAGATAGGTTTGAGTTCAAATTTTGCATTGTGTGAGCTTCTACAGATTTTAGACAAGGACCGTTTTTACTAAGTAAAAGGGTGGAGAGGTTCCTGGGGTGGATTCCTAAGCAGTGCTTGTAAACCATCGCGTGCAATGAGCCAGATGGAGTACCATGAGGGTTGCTATTTGTTGTTTTTAACAACTAATCAAGAGTGAGTGAACAACTATTTATAAACTAGATCTCCTATTTTTCAGAATGCTCTTCTACGTATAAATATGAAATGATAAAGATGTCAAATATCTCAGAGGCTATAGCTGGGAACCCGACTGTGAAAGTATGTGATATCTGAACACATACTAGAAAGCTCTGCATGTGTGTTGTCCTTCAGCATAATTCGGAAGGGAAAACAGTCGATCAAGGGATGTATTGGAACATGTCGGAGTAGAAATTGTTCCTGATGTGCCAGAACTTCGACCCTTTCTCTGAGAGAGATGATCGTGCCTATAAATAGTAGGACCAATGTTGTGATTAACATCATCAGGCTTGGAATGAATTCTCTCTAAAAATAAAATGATGTATGATTTGTTGTTGGCATCCCCTTTATTAATTCATTAAATTTCTGGATTTGGGTTGTGACCCAGGGTGCATTAACTTAAAAGATTCACTAAAGCAGCACATAGCACTGGGAACTCTGGCTCCGAAAAACTTTGTTATATATATCAAGGATGTTCTGGCTTTACATTTTATTTATTAGCTGTAAATACATGTGTGGATGTGTAAATGGAGCTTGTACATATTGGAAAGGTCATTGTGGCTATCTGCATTTATAAATGTGTGGTGCTAACTGTATGTGTCTTTATCAGTGATGGTCTCACAGAGCCAACTCACTCTTATGAAATGGGCTTTAACAAAACAAGAAAGAAACGTACTTAACTGTGTGAAGAAATGGAATCAGCTTTTAATAAAATTGACAACATTTTATTACCACA";
    private final String LPL_CDS = "ATGGAGAGCAAAGCCCTGCTCGTGCTGACTCTGGCCGTGTGGCTCCAGAGTCTGACCGCCTCCCGCGGAGGGGTGGCCGCCGCCGACCAAAGAAGAGATTTTATCGACATCGAAAGTAAATTTGCCCTAAGGACCCCTGAAGACACAGCTGAGGACACTTGCCACCTCATTCCCGGAGTAGCAGAGTCCGTGGCTACCTGTCATTTCAATCACAGCAGCAAAACCTTCATGGTGATCCATGGCTGGACGGTAACAGGAATGTATGAGAGTTGGGTGCCAAAACTTGTGGCCGCCCTGTACAAGAGAGAACCAGACTCCAATGTCATTGTGGTGGACTGGCTGTCACGGGCTCAGGAGCATTACCCAGTGTCCGCGGGCTACACCAAACTGGTGGGACAGGATGTGGCCCGGTTTATCAACTGGATGGAGGAGGAGTTTAACTACCCTCTGGACAATGTCCATCTCTTGGGATACAGCCTTGGAGCCCATGCTGCTGGCATTGCAGGAAGTCTGACCAATAAGAAAGTCAACAGAATTACTGGCCTCGATCCAGCTGGACCTAACTTTGAGTATGCAGAAGCCCCGAGTCGTCTTTCTCCTGATGATGCAGATTTTGTAGACGTCTTACACACATTCACCAGAGGGTCCCCTGGTCGAAGCATTGGAATCCAGAAACCAGTTGGGCATGTTGACATTTACCCGAATGGAGGTACTTTTCAGCCAGGATGTAACATTGGAGAAGCTATCCGCGTGATTGCAGAGAGAGGACTTGGAGATGTGGACCAGCTAGTGAAGTGCTCCCACGAGCGCTCCATTCATCTCTTCATCGACTCTCTGTTGAATGAAGAAAATCCAAGTAAGGCCTACAGGTGCAGTTCCAAGGAAGCCTTTGAGAAAGGGCTCTGCTTGAGTTGTAGAAAGAACCGCTGCAACAATCTGGGCTATGAGATCAATAAAGTCAGAGCCAAAAGAAGCAGCAAAATGTACCTGAAGACTCGTTCTCAGATGCCCTACAAAGTCTTCCATTACCAAGTAAAGATTCATTTTTCTGGGACTGAGAGTGAAACCCATACCAATCAGGCCTTTGAGATTTCTCTGTATGGCACCGTGGCCGAGAGTGAGAACATCCCATTCACTCTGCCTGAAGTTTCCACAAATAAGACCTACTCCTTCCTAATTTACACAGAGGTAGATATTGGAGAACTACTCATGTTGAAGCTCAAATGGAAGAGTGATTCATACTTTAGCTGGTCAGACTGGTGGAGCAGTCCCGGCTTCGCCATTCAGAAGATCAGAGTAAAAGCAGGAGAGACTCAGAAAAAGGTGATCTTCTGTTCTAGGGAGAAAGTGTCTCATTTGCAGAAAGGAAAGGCACCTGCGGTATTTGTGAAATGCCATGACAAGTCTCTGAATAAGAAGTCAGGCTGA";
    private String N_DNA;

    private int[] CDS_start = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] CDS_end = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    private String CDS1 = "ATGGAGAGCAAAGCCCTGCTCGTGCTGACTCTGGCCGTGTGGCTCCAGAGTCTGACCGCCTCCCGCGGAGGGGTGGCCGCCGCCGACC";
    private String CDS2 = "AAAGAAGAGATTTTATCGACATCGAAAGTAAATTTGCCCTAAGGACCCCTGAAGACACAGCTGAGGACACTTGCCACCTCATTCCCGGAGTAGCAGAGTCCGTGGCTACCTGTCATTTCAATCACAGCAGCAAAACCTTCATGGTGATCCATGGCTGGACGGTAA";
    private String CDS3 = "CAGGAATGTATGAGAGTTGGGTGCCAAAACTTGTGGCCGCCCTGTACAAGAGAGAACCAGACTCCAATGTCATTGTGGTGGACTGGCTGTCACGGGCTCAGGAGCATTACCCAGTGTCCGCGGGCTACACCAAACTGGTGGGACAGGATGTGGCCCGGTTTATCAACTGGATGGAGG";
    private String CDS4 = "AGGAGTTTAACTACCCTCTGGACAATGTCCATCTCTTGGGATACAGCCTTGGAGCCCATGCTGCTGGCATTGCAGGAAGTCTGACCAATAAGAAAGTCAACAGAATTACTGG";
    private String CDS5 = "CCTCGATCCAGCTGGACCTAACTTTGAGTATGCAGAAGCCCCGAGTCGTCTTTCTCCTGATGATGCAGATTTTGTAGACGTCTTACACACATTCACCAGAGGGTCCCCTGGTCGAAGCATTGGAATCCAGAAACCAGTTGGGCATGTTGACATTTACCCGAATGGAGGTACTTTTCAGCCAGGATGTAACATTGGAGAAGCTATCCGCGTGATTGCAGAGAGAGGACTTGGAG";
    private String CDS6 = "ATGTGGACCAGCTAGTGAAGTGCTCCCACGAGCGCTCCATTCATCTCTTCATCGACTCTCTGTTGAATGAAGAAAATCCAAGTAAGGCCTACAGGTGCAGTTCCAAGGAAGCCTTTGAGAAAGGGCTCTGCTTGAGTTGTAGAAAGAACCGCTGCAACAATCTGGGCTATGAGATCAATAAAGTCAGAGCCAAAAGAAGCAGCAAAATGTACCTGAAGACTCGTTCTCAGATGCCCTACAAAG";
    private String CDS7 = "TCTTCCATTACCAAGTAAAGATTCATTTTTCTGGGACTGAGAGTGAAACCCATACCAATCAGGCCTTTGAGATTTCTCTGTATGGCACCGTGGCCGAGAGTGAGAACATCCCATTCACTCTG";
    private String CDS8 = "CCTGAAGTTTCCACAAATAAGACCTACTCCTTCCTAATTTACACAGAGGTAGATATTGGAGAACTACTCATGTTGAAGCTCAAATGGAAGAGTGATTCATACTTTAGCTGGTCAGACTGGTGGAGCAGTCCCGGCTTCGCCATTCAGAAGATCAGAGTAAAAGCAGGAGAGACTCAGAAAAAG";
    private String CDS9 = "GTGATCTTCTGTTCTAGGGAGAAAGTGTCTCATTTGCAGAAAGGAAAGGCACCTGCGGTATTTGTGAAATGCCATGACAAGTCTCTGAATAAGAAGTCAGGCTG";

    private String[] CDSs = {CDS1, CDS2, CDS3, CDS4, CDS5, CDS6, CDS7, CDS8, CDS9};


    public DefectAnalyse(String path, int start, int end, double tv1, double tv2) {
        defectRecognition = new DefectRecognition(path);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);
        analyseDaoImpl = new AnalyseDaoImpl();
        geneDao=new GeneDaoImpl();
        N_DNA = dataMap.get("N_DNA");
        Dna stdDna=geneDao.matchGeneByFragment(N_DNA.substring(0,20));
        stdGene=stdDna.getSort();
        stdCDS=stdDna.getCds();
        CDS=new ArrayList<String>();

        for (int i = 0; i < CDSs.length; i++) {
            CDS_start[i] = LPL.indexOf(CDSs[i]);
            CDS_end[i] = LPL.indexOf(CDSs[i]) + CDSs[i].length() - 1;
        }
    }

    /**
     * 找出CDS片段
     */
    public void getCDS(){
        char gene[]=stdGene.toCharArray();
        char cds[]=stdCDS.toCharArray();
        String cdsFragment="";
        int gStart=0;
        int cStart=0;
        int length=0;
        int geneCursor=0;
        for(int i=0;i<cds.length;){
            for(int j=geneCursor;i<gene.length;j++){
                String candidate="";
                int increment=0;
                while(cds[i+increment]==gene[j+increment]){
                    candidate=candidate+gene[j+increment];
                    increment++;
                }
                if(candidate.length()>cdsFragment.length()){
                    cdsFragment=candidate;
                    length=candidate.length();
                    gStart=j+length;
                    cStart=i+length;
                }
            }
            CDS.add(cdsFragment);
            cdsFragment="";
            geneCursor=gStart;
            i=cStart;
        }
    }

    /**
     * 异常在完整DNA片段上的真实位置
     */
    @Override
    public Map<String, String> getRealPosition() {

        Map<String, String> positionMap = new HashMap<>();
        List<String> changedList = new ArrayList<>();

        //TODO 目前只针对LPL, 后面需要首先判断是什么类型的基因, 再找位置
        int firstPosition = LPL.indexOf(N_DNA);
        //int firstPosition = stdGene.getSort.indexOf(N_DNA);

        for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
            changedList.add(dataMap.get("sf_info").split(";")[i]);
            String[] changedInfo = changedList.get(i).split(":");
            positionMap.put(changedInfo[0], String.valueOf(Integer.parseInt(changedInfo[0]) + firstPosition + 1));
        }

        return positionMap;
    }

    /**
     * 异常在完整CDS片段上的真实位置
     */
    @Override
    public Map<String, String> getCDSPosition() {
        Map<String, String> CDSPositionMap = new HashMap<>();
        Map<String, String> positionMap = getRealPosition();
        List<String> changedList = new ArrayList<>();

        for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
            changedList.add(dataMap.get("sf_info").split(";")[i]);
            String[] changedInfo = changedList.get(i).split(":");

            // 先判断在CDS上，再计算异常在完整CDS片段上的真实位置
            int realPosition = Integer.parseInt(positionMap.get(changedInfo[0]));
            int CDSPosition = 0;
            if (isCDS(realPosition)) {
                for (int j = 0; j < CDSs.length; j++) {
                    if (CDS_end[j] < realPosition) {
                        CDSPosition += (CDS_end[j] - CDS_start[j]);
                    } else {
                        CDSPosition += (realPosition - CDS_start[j]);
                    }
                }
            }
            CDSPositionMap.put(changedInfo[0], String.valueOf(CDSPosition));
        }

        return CDSPositionMap;
    }


    /**
     * 异常所在DNA片段区域
     */
    @Override
    public Map<String, String> getArea() {
        Map<String, String> areaMap = new HashMap<>();
        Map<String, String> positionMap = getRealPosition();
        List<String> changedList = new ArrayList<>();

        for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
            changedList.add(dataMap.get("sf_info").split(";")[i]);
            String[] changedInfo = changedList.get(i).split(":");

            if (isCDS(Integer.parseInt(positionMap.get(changedInfo[0])))) {
                areaMap.put(changedInfo[0], "outer");
            } else {
                areaMap.put(changedInfo[0], "inner");
            }
        }
        System.out.println("position map: " + positionMap);
        System.out.println("area map: " + areaMap);

        return areaMap;
    }


    /**
     * key: 点位
     * value: 变化信息，正常基因=>异常基因
     *
     * @return
     */
    @Override
    public Map<String, String> getChangedInfo() {
        Map<String, String> changedMap = new HashMap<>();
        List<String> changedList = new ArrayList<>();
        for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
            changedList.add(dataMap.get("sf_info").split(";")[i]);
            String[] changedInfo = changedList.get(i).split(":");
            changedMap.put(changedInfo[0], changedInfo[1].charAt(0) + "=>" + changedInfo[1].charAt(1));
        }

        System.out.println("changed map: " + changedMap);

        return changedMap;
    }

    /**
     * 氨基酸分析
     */
    @Override
    public Map<String, String> getAcidAnalysis() {
        Map<String, String> acidMap = new HashMap<>();
        Map<String, String> changedMap = getChangedInfo();
        Map<String, String> areaMap = getArea();
        Map<String, String> CDSPositionMap = getCDSPosition();
        String[] ycArray = dataMap.get("yc").split(";");
        for (String st : ycArray) {

            // 非内显子, 继续分析
            if (!areaMap.get(st).equals("inner")) {
                int CDSPostion = Integer.parseInt(CDSPositionMap.get(st));
                String U_secret = ""; // 异常密码子
                String N_secret = ""; // 正常密码子

                // 余数为0，向前拼接两位碱基构成密码子
                if (CDSPostion % 3 == 0) {
                    N_secret = LPL_CDS.substring(CDSPostion - 2, CDSPostion + 1);
                    U_secret = LPL_CDS.substring(CDSPostion - 2, CDSPostion) + changedMap.get(st).substring(3);
                }
                // 余数为1，向后拼接两位碱基构成密码子
                else if (CDSPostion % 3 == 1) {
                    N_secret = LPL_CDS.substring(CDSPostion, CDSPostion + 3);
                    U_secret = changedMap.get(st).substring(3) + LPL_CDS.substring(CDSPostion, CDSPostion + 2);
                }
                // 余数为2，取一前一后两位碱基构成密码子
                else if (CDSPostion % 3 == 2) {
                    N_secret = LPL_CDS.substring(CDSPostion - 1, CDSPostion + 2);
                    U_secret = LPL_CDS.substring(CDSPostion - 1, CDSPostion) + changedMap.get(st).substring(3) + LPL_CDS.substring(CDSPostion + 1, CDSPostion + 2);
                }

                // 找密码子对应的氨基酸
                acidMap.put(changedMap.get(st), analyseDaoImpl.getSecret(N_secret).getChs_name() + "=>" + analyseDaoImpl.getSecret(U_secret).getChs_name());
            }

        }
        return acidMap;
    }

    /**
     * @param position 基因真实位置
     * @return 基因是否在CDS序列上
     */
    private boolean isCDS(int position) {
        boolean res = false;
        for (int i = 0; i < CDSs.length; i++) {
            if (CDS_start[i] <= position && position <= CDS_end[i]) {
                res = true;
                break;
            }
        }

        return res;

    }
}
