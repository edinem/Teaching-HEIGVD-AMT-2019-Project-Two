#!/bin/bash

SCRIPT_NAME=generate.sql
PASS_HASH=734b75b6d13217f24b0b092a8a5dbc7e9bd40c14a333db482c17f5e737979773ee13e37ee904f4e55e5939aaebb09dee50674ae977d538b316adb806a67fddc7
RAND_LASTNAMES=
RAND_FIRSTNAMES=
RAND_USERNAME=
NUMBER_ITERATIONS=$1
NUMBER_USERS=20
NUMBER_CALENDARS=$((NUMBER_ITERATIONS/NUMBER_USERS))
ROLES_ARRAY=(OWNER EDITOR VIEWVER)

#echo "DROP DATABASE IF EXISTS AMT_CALENDAR;" >>
#echo "CREATE DATABASE IF NOT EXISTS AMT_CALENDAR;" >> generate.sql
echo "use AMT_CALENDAR;" > $SCRIPT_NAME

## DELETING CURRENT ENTRIES
#echo "DELETE FROM user;" >> $SCRIPT_NAME
#echo "DELETE FROM calendar;" >> $SCRIPT_NAME
#echo "DELETE FROM user_has_calendar;" >> $SCRIPT_NAME

## GENERATING USERS ENTRIES WITH SPECIFIED HASH
echo "Generating ${NUMBER_USERS} users with the password #Welcome123 ...."
echo "INSERT INTO user (email)" >> $SCRIPT_NAME
echo "VALUES" >> $SCRIPT_NAME
for (( i=1; i<=$NUMBER_USERS; i++ ))
do
if [ $i -eq "$NUMBER_USERS" ]; then
	endOfLine=";"
 else
	endOfLine=","
 fi
 echo "(\"email${i}@email.com\")${endOfLine}" >> $SCRIPT_NAME

done

## GENERATING CALENDARS ENTRIES
echo "Generating ${NUMBER_CALENDARS} calendars ...."
echo "INSERT INTO calendar(name)" >> $SCRIPT_NAME
echo "VALUES" >> $SCRIPT_NAME
for (( j=1; j<=$NUMBER_CALENDARS; j++ ))
do
if [ $j -eq "$NUMBER_CALENDARS" ]; then
        endOfLine=";"
 else
        endOfLine=","
 fi
 echo "(\"calendar${j}\")${endOfLine}" >> $SCRIPT_NAME
done

## GENERATINS USER_HAS_ACCESS ENTRIES
userId=1
calendarId=1

echo "Generating ${NUMBER_ITERATIONS} accesses ...."
echo "INSERT INTO user_has_calendar(user_id, calendar_id, role)" >> $SCRIPT_NAME
echo "VALUES" >> $SCRIPT_NAME
for (( k=1; k<=$NUMBER_ITERATIONS; k++ ))
do
  if [ $k -eq "$NUMBER_ITERATIONS" ]; then
     endOfLine=";"
  else
     endOfLine=","
  fi

    roleId=$((($k*7)%3))
  echo "(\"email${userId}@email.com\", ${calendarId}, \"${ROLES_ARRAY[$roleId]}\")${endOfLine}" >> $SCRIPT_NAME
  tmp=$((calendarId % NUMBER_CALENDARS)) 
  if [ $tmp -eq "0" ]; then
      ((userId++))
      calendarId=0
  fi
  ((calendarId++))
  
done        


echo "Script ${SCRIPT_NAME} generated !"
