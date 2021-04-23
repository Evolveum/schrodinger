#!/bin/sh

filename="/opt/training/midpoint-labs/flatfiles/after-mod.log"
echo "--------------------" >> "$filename"
echo "  [`date`]" >> "$filename"
echo "  Card data update request for user: $midpoint_name ($midpoint_givenName $midpoint_familyName)"  >> "$filename"
echo "--------------------" >> "$filename"
