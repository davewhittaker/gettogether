if test -f .sbtconfig; then
  . .sbtconfig
fi
exec java ${SBT_OPTS} -Xmx1024M -jar sbt-launch.jar "$@"
