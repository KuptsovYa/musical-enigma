#!/usr/bin/env bash
set -euo pipefail

# Decompile all jar files under a given directory using CFR decompiler.
# For each jar, sources are generated in a folder with the same name
# as the jar (without the .jar extension) located alongside the jar file.
# This script downloads CFR decompiler jar automatically if not present.

NEXUS_DIR=${1:-nexus-3.77.2-02}
DECOMPILER_JAR=${DECOMPILER_JAR:-cfr.jar}
# Default location of the CFR decompiler on Maven Central.  The URL can be
# overridden by setting the CFR_URL environment variable.
CFR_URL=${CFR_URL:-https://repo1.maven.org/maven2/org/benf/cfr/0.152/cfr-0.152.jar}

# Download CFR decompiler if not exists
if [[ ! -f "$DECOMPILER_JAR" ]]; then
  echo "Downloading CFR decompiler to $DECOMPILER_JAR..." >&2
  curl -L "$CFR_URL" -o "$DECOMPILER_JAR"
fi

# Iterate over jar files
find "$NEXUS_DIR" -type f -name '*.jar' | while read -r jar; do
  outdir="${jar%.jar}"
  echo "Decompiling $jar -> $outdir" >&2
  mkdir -p "$outdir"
  java -jar "$DECOMPILER_JAR" "$jar" --outputdir "$outdir" >/dev/null 2>&1
  echo "Done $jar" >&2
done