Set WShell = CreateObject("WScript.Shell")
Set WMI = GetObject("winmgmts:{impersonationLevel=impersonate}").ExecQuery("SELECT * FROM Win32_Process")
For Each Process In WMI
	If InStr(1,Process.Name,"iexplore.exe")>0 then
	ProcessId = process.ProcessId
	exit for
	End if
Next
WShell.AppActivate(ProcessId)
set Wshell = Nothing
set WMI = nothing
