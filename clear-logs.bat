@rem Navigate to the project directory
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit

@rem Delete log folder outputs
rm -r logs/
rm -r mayonez-base/logs/
echo "Cleared all log files"