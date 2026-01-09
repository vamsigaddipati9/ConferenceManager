Overview of test files for WolfProceedings

conference0.txt - valid file with only a conference name
conference1.txt - valid file with three sessions and 15 items in the proceedings
conference2.txt - valid file with no sessions and several items in the proceedings
conference3.txt - invalid file where session title is missing - creates conference with no sessions or accepted items
conference4.txt - invalid file where session duration is missing - creates conference with no sessions or accepted items
conference5.txt - invalid file where session duration is less than 5 - creates conference with no sessions or accepted items
conference6.txt - invalid file where session duration is over 120 - creates conference with no sessions or accepted items 
conference7.txt - invalid file where item type is missing - creates conference with no sessions or accepted items
conference8.txt - invalid file where item authors are missing - creates conference with no sessions or accepted items
conference9.txt - invalid file where title is missing - creates conference with no sessions or accepted items
conference10.txt - invalid file where a session is included for an item in the unassigned items list (# +++) - creates conference with no sessions or accepted items
conference11.txt - valid file where some papers have non-default durations and one LT is invalid
