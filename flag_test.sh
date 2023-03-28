#!/bin/sh

# Functions

show_help() {
	echo "Usage: run [--help] [--nobuild/-n] [--engine/-e gl/awt] [--log/-l on/off]"
}

engine="gl"
log="on"

# Read Arguments
while :; do
	case $1 in
		--help|-h)
			show_help
			exit 0
			;;
		--nobuild|-n)
			nobuild=true
			;;
		--engine|-e)
			if [ "$2" ]; then
     	   		engine="$2"
                shift
         	else
                echo 'Option "--engine" requires one argument'
                exit 1
         	fi
         	;;
     	--log|-l)
			if [ "$2" ]; then
     	   		log="$2"
                shift
         	else
     			echo 'Option "--log" requires one argument'
                exit 1
         	fi
         	;;
     	-*)
			echo "Invalid option \"$1\""
            exit 1
			;;
     	*)
			break
	esac
	shift
done

echo "Engine: $engine";
echo "Logs: $log";
echo "Args: $@"