#include <stdio.h>
#include <ctype.h>
#include <string.h>


typedef struct {
    int letterCount;
    int digitCount;
    int whitespaceCount;
    int operatorCount;
    int delimiterCount;
    int specialCount;
    int totalCount;
} CharStats;


void classifyCharacter(char ch, CharStats *stats) {

    stats->totalCount++;   


    if (isalpha((unsigned char) ch)) {
        stats->letterCount++;
    }

    else if (isdigit((unsigned char) ch)) {
        stats->digitCount++;
    }

    else if (isspace((unsigned char) ch)) {
        stats->whitespaceCount++;
    }

    else if (strchr("+-*/=<>!&|%^~", ch) != NULL) {
        stats->operatorCount++;
    }

    else if (strchr("(){}[];,.:\"'", ch) != NULL) {
        stats->delimiterCount++;
    }

    else {
        stats->specialCount++;
    }
}


void initStats(CharStats *stats) {
    stats->letterCount = 0;
    stats->digitCount = 0;
    stats->whitespaceCount = 0;
    stats->operatorCount = 0;
    stats->delimiterCount = 0;
    stats->specialCount = 0;
    stats->totalCount = 0;
}


void printSummary(CharStats *stats) {
    printf("\n");
    printf("=================================================\n");
    printf("        SOURCE CHARACTER ANALYSIS SUMMARY        \n");
    printf("=================================================\n");
    printf("%-22s : %6d\n", "Letters",     stats->letterCount);
    printf("%-22s : %6d\n", "Digits",      stats->digitCount);
    printf("%-22s : %6d\n", "Whitespace",  stats->whitespaceCount);
    printf("%-22s : %6d\n", "Operators",   stats->operatorCount);
    printf("%-22s : %6d\n", "Delimiters",  stats->delimiterCount);
    printf("%-22s : %6d\n", "Special Symbols", stats->specialCount);
    printf("-------------------------------------------------\n");
    printf("%-22s : %6d\n", "TOTAL CHARACTERS", stats->totalCount);
    printf("=================================================\n");
}

void analyzeStream(FILE *inputStream, CharStats *stats) {
    int ch;   

    while ((ch = fgetc(inputStream)) != EOF) {
        classifyCharacter((char) ch, stats);
    }
}


int main(void) {
    CharStats stats;
    initStats(&stats);

    int choice;
    char filename[256];

    printf("=================================================\n");
    printf("   COMPILER WORKSPACE SETUP - CHARACTER ANALYZER \n");
    printf("=================================================\n");
    printf("Choose input source:\n");
    printf("  1. Type source code directly (keyboard)\n");
    printf("  2. Read source code from a text file\n");
    printf("Enter choice (1 or 2): ");

    if (scanf("%d", &choice) != 1) {
        printf("Invalid input. Exiting.\n");
        return 1;
    }
    getchar(); 

    if (choice == 1) {
        printf("\nType or paste your source code below.\n");
        printf("When finished, press ENTER then Ctrl+D (Linux/Mac) or Ctrl+Z (Windows):\n");
        printf("-------------------------------------------------\n");

        analyzeStream(stdin, &stats);  
    }
    else if (choice == 2) {
        printf("Enter the filename (e.g. sample.c): ");
        if (scanf("%255s", filename) != 1) {
            printf("Invalid filename input. Exiting.\n");
            return 1;
        }

        FILE *fp = fopen(filename, "r");
        if (fp == NULL) {
            printf("ERROR: Could not open file '%s'.\n", filename);
            printf("Please check the filename and try again.\n");
            return 1;
        }

        analyzeStream(fp, &stats);
        fclose(fp);   
    }
    else {
        printf("Invalid choice. Please run the program again and enter 1 or 2.\n");
        return 1;
    }

    printSummary(&stats);

    return 0;
}
