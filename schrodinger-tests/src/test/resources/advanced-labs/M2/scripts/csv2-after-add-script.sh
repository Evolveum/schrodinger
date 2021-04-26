#!/bin/sh

filename="./after-add.log"
echo "--------------------" >> "$filename"
echo "  [`date`]" >> "$filename"
echo "  Card data create request for user: $midpoint_name ($midpoint_givenName $midpoint_familyName)/PIN=$pin"  >> "$filename"
echo "--------------------" >> "$filename"
