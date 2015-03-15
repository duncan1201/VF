set SRC_HOME=D:\vf\x32
set ZIP_DIR_DRIVE=D:
set ZIP_DIR=D:\sequenceanalysis_vf\trunk\netbeans\vf\dist

rd %SRC_HOME%\harness /q /s
rd %SRC_HOME%\ide /q /s
rd %SRC_HOME%\platform /q /s
rd %SRC_HOME%\vf /q /s

%ZIP_DIR_DRIVE%
cd %ZIP_DIR%
jar xf %ZIP_DIR%\vf.zip

xcopy %ZIP_DIR%\vf %SRC_HOME% /e /y

rd %ZIP_DIR%\vf /s /q

%SRC_HOME%\bin\vf.exe --console suppress