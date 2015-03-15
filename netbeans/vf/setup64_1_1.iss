; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "VectorFriends"
#define MyAppVersion "1.1"
#define MyAppPublisher "BioFriends(Pte Ltd)"
#define MyAppURL "http://www.vectorfriends.com/"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{C692C1FE-C420-421B-A879-625F3B61D059}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DefaultGroupName=VectorFriends
LicenseFile=D:\sequenceanalysis_vf\trunk\netbeans\vf\vf_eula.rtf
OutputDir=D:\vf_dist
OutputBaseFilename=VectorFriends_win64_1_1
Compression=lzma
SolidCompression=yes
AlwaysRestart=no
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: checkedonce

[Files]
Source: "D:\vf\x64\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs;
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\bin\vf64.exe";
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\bin\vf64.exe";  Tasks: desktopicon;
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"

[Run]
Filename: "{app}\bin\vf64.exe"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent;

