#!/bin/sh

show_help() {
	echo "Usage: run [--help] [--engine gl/awt] [--log on/off]"
}

engine="gl"
log="on"

while :; do
	case $1 in
		-h|--help)
			show_help
			exit 0
			;;
		-e|--engine)
			if [ "$2" ]; then
     	   		engine="$2"
                shift
         	else
                echo 'Option "--engine" requires one argument'
                exit 1
         	fi
         	;;
     	-l|--log)
			if [ "$2" ]; then
     	   		log="$2"
                shift
         	else
     			echo 'Option "--log" requires one argument'
                exit 1
         	fi
         	;;
     	*)
			break
	esac
	shift
done

echo "Engine: $engine";
echo "Logs: $log";
echo "Args: $@"