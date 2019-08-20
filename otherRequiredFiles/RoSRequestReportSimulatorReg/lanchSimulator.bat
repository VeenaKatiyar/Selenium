cd C:\Temp\Solutions_Test_Automation\Automation_Framework\otherRequiredFiles\RoSRequestReportSimulatorReg

if not DEFINED IS_MINIMIZED set IS_MINIMIZED=1 && start "" /max "RoSRequestReportSimulator.exe" %* && exit
exit