#!/bin/zsh

help() {
    echo "How to use test suite:"
    echo "\tCreate a file with the format test_[a-z]+.xy in the tests directory"
    echo "\tMake the first line a comment with the expected exit code: E.g. // exit 1"
    echo "\tIf there's any stdout you want to test, create a file with the same name but with .txt instead of .xy"
    echo "\tThe script will then do a diff compare to make sure it's the same"
    echo "\tNo need to provide the .txt file if there's no output to test"
    echo
}


## Variables 
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RESET='\033[0m'
PASS=0
FAIL=0
TOTAL=0

gradle build >> /dev/null 2>&1

# Helpful details for shell script
if [ "$#" -ne "0" ]
then
    if [ "$1" = "--help" ] || [ "$1" = "-h" ]
    then 
        help
    fi
fi


while IFS= read -r file
do
    rawName=$file
    file=$(basename $file)  
    # Ignoring any other random files that aren't .txt or .xy
    if ! (echo $file | grep -Eq "test_[0-9]+\.(xy|txt)$")
    then
        echo "Skipping over ${YELLOW}${file}${RESET}: invalid filename"
        continue
    fi

    # If it's not a .xy file, ignore
    if ! echo $file | grep -Eq "\.xy$"
    then 
        continue
    fi


    EXIT_CODE=$(head -n1 "$rawName" | grep -Eo "[0-9]+$")
    if  [ "$EXIT_CODE" = "" ]
    then 
        echo "Skipping over ${YELLOW}${file}${RESET}: please provide exit code in first line"
        continue
    fi

    SUMMARY=$(head -n2 "$rawName" | tail -n1 | sed -e "s/\/\/ //g" | tr a-z A-Z)

    # Attempt to build the executable
    java -jar build/libs/javaCompiler-1.0-SNAPSHOT.jar "$rawName"  >> /dev/null 2>&1

    # Build failed
    if [ "$?" -ne "0" ]
    then
        echo "${SUMMARY}: ${RED}Build error${RESET} for ${YELLOW}${file}${RESET}, counting as fail"
        FAIL=$((FAIL + 1))
        TOTAL=$((TOTAL + 1)) 
        continue
    fi 

    # Now execute commands and compare to the test_one.txt file
    testFile=$(echo "$rawName" | sed -E "s/xy/txt/g")
    if [ -f "$testFile" ]
    then 
        # Also need to compare stdout
        ./out > "tests/current_output"
        if [ "$EXIT_CODE" -ne "$?" ]
        then 
            echo "${SUMMARY}: ${RED}Fail${RESET} for ${YELLOW}${file}${RESET}, differing exit codes"
            FAIL=$((FAIL + 1))
        else

            if diff "tests/current_output" "$testFile" > /dev/null
            then
                # They are identical
                echo "${SUMMARY}: ${GREEN}Pass${RESET} for ${YELLOW}${file}${RESET}, same exit code and stdout"
                PASS=$((PASS + 1))
            else
                # They aren't identical
                echo "${SUMMARY}: ${RED}Fail${RESET} for ${YELLOW}${file}${RESET}, differing stdout, same exit code"
                FAIL=$((FAIL + 1))
            fi 
            TOTAL=$((TOTAL + 1)) 
        fi

    else
        # Don't bother comparing stdout
        ./test
        if [ "$EXIT_CODE" -eq "$?" ]
        then
            echo "${SUMMARY}: ${GREEN}Pass${RESET} for ${YELLOW}${file}${RESET}, same exit code"
            PASS=$((PASS + 1))
        else 
            echo "${SUMMARY}: ${RED}Fail${RESET} for ${YELLOW}${file}${RESET}, differing exit codes"
            FAIL=$((FAIL + 1))
        fi 
        TOTAL=$((TOTAL + 1)) 
    fi

done < <(find "tests" -type f)

if [ -f "tests/current_output" ]
then 
    rm tests/current_output
fi

if [ -f "test" ]
then 
    rm test
fi

if [ -f "out.asm" ]
then 
    rm "out.asm"
fi

if [ -f "out.o" ]
then 
    rm "out.o"
fi

echo
echo "Test suite completed: "

if [ "$PASS" = "$TOTAL" ] 
then 
    echo "\t${GREEN}All passed${RESET}"
    echo "\t${TOTAL} total"
else
    echo "\t${GREEN}${PASS} passed${RESET}"
    echo "\t${RED}${FAIL} failed${RESET}"
    echo "\t${TOTAL} total"
fi