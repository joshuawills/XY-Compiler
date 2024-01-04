#!/bin/sh

filename="$1" # Should be a .asm file
rawName=$(echo "$filename" | grep -o "^[^.]*")
oName="${rawName}.o"
nasm -felf64 "$filename" && echo "Sucessfully assembled"
ld "$oName" -o "$rawName" && echo "${rawName} executable created"
echo "Removing the linker..."
rm "$oName"