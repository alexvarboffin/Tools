rem @echo off


setlocal
cd /d %~dp0

rem Modify this data
rem set SCREENSHOT_WIDTH=720
rem  SCREENSHOT_HEIGHT=1280

rem @16:9 16:9 9:16 1080 portrait: 1080/9 120   120x16 =>1920
set SCREENSHOT_WIDTH=1080
set SCREENSHOT_HEIGHT=1920




set SCREENSHOT_COUNT=5
set WW=D:\dropbox\@imagemagick\build\_wallpapers\11.jpg
rem set WW=00.jpg

set BORDER_WIDTH=55
rem set BORDER_WIDTH=0

set /a w=%SCREENSHOT_WIDTH%*%SCREENSHOT_COUNT%
set /a b=%BORDER_WIDTH%*%SCREENSHOT_COUNT%*2
set /a all=%w%+%b%
set /a NEW_SCREEN_WIDTH=%SCREENSHOT_WIDTH%+%BORDER_WIDTH%*2

set PATH=%PATH%;C:\Program Files\ImageMagick-7.0.10-Q16\

rem 719x1438
rem set BACKGROUND_FONE_WIDTH=1080x1920
set BACKGROUND_FONE_WIDTH=%all%
set /a BACKGROUND_FONE_HEIGHT=%SCREENSHOT_HEIGHT%+%BORDER_WIDTH%




rem gray 00ccff "#00000000"

rem montage -tile 9x -background none -fill none -define jpeg:size=719x1920 -geometry 719x1920 "*.png" all/gr.png

montage -tile %SCREENSHOT_COUNT%x -background none -fill none -define jpeg:size=%SCREENSHOT_WIDTH%x%SCREENSHOT_HEIGHT% -geometry %SCREENSHOT_WIDTH%x%SCREENSHOT_HEIGHT%+%BORDER_WIDTH%+%BORDER_WIDTH% "screenshot/*.png" montage.png









rem MAX_SIZE
rem montage -tile 9x -background none -fill none -define jpeg:size=1920x3840 -geometry 1920x3840+2+2 "*.png" all/gr.png




mkdir cropped
mkdir tmp


set MM=montage.png


rem convert %WW% -resize 6507x1924! tmp_bg.png

rem Generaate background fone H>image.Hight
convert %WW% -resize %BACKGROUND_FONE_WIDTH%x%BACKGROUND_FONE_HEIGHT%! background.png

rem convert tmp/tmp_bg.png %MM% /* -geometry +6507+1924*/ -composite venice_pinned.jpg


convert background.png %MM% -composite composite.jpg


rem convert -composite %WW% -gravity center %MM%  result3.png

rem convert -crop 1920x3840! venice_pinned.jpg cropped/cropped_%d.png
"convert.exe" -crop %NEW_SCREEN_WIDTH%x composite.jpg cropped/cropped_%d.png

pause
