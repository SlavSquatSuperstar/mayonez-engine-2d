#!/bin/sh

# Script Name: run.sh
# Purpose:     Runs the project for macOS users.
# Usage:       ./run.sh [-h/--help] [-e/--engine gl/awt]"
# Author:      SlavSquatSuperstar

# Navigate to the project directory
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit

# Initialize variables
USE_GL=true

## Functions ##

# Show help and exit with a code
show_help() {
  echo "Usage: run.sh [-h/--help] [-e/--engine gl/awt]"
  exit "$1"
}

# Configure the program to run using GL/AWT
set_engine_type() {
  case $1 in
  "gl")
    USE_GL=true
    ;;
  "awt")
    USE_GL=false
    ;;
  "")
    echo "Option \"--engine\" requires one argument"
    show_help 1
    ;;
  *)
    echo "Invalid engine type \"$1\"."
    show_help 1
    ;;
  esac
}

# Launch the jar with VM and CL args
run_jar() {
  if [ "$2" ]; then
    java "$2" -jar mayonez*.jar "--engine" "$1"
  else
    java -jar mayonez*.jar "--engine" "$1"
  fi
  exit $?
}

## Script Start ##

# Parse user arguments
while :; do
  case $1 in
  --help | -h)
    show_help 0
    ;;
  --engine | -e)
    set_engine_type "$2"
    shift
    ;;
  "")
    # End of arguments
    break
    ;;
  *)
    echo "Invalid option \"$1\"."
    show_help 1
    ;;
  esac
  shift
done

# Check if Java is installed
if [ "$(java --version)" ]; then
  echo "Java is installed."
else
  echo "Please install Java to run this program."
  exit 1
fi

# Run the compiled .jar file
if $USE_GL; then
  echo "Launching with OpenGL Engine."
  # Make GLFW start on first thread on macOS
  run_jar "gl" "-XstartOnFirstThread"
else
  echo "Launching with AWT Engine."
  run_jar "awt"
fi