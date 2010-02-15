@echo off & color 02
cls
@set Var=%DATE%
@set Selected=2
@set Disk=F
goto Lang

################
#   語系選單
################
:Lang
@set Language=en
set /p Language="Choose Systems Language (en/cn/tw) ? "
goto %Language%


################
#  主系統選單
################
:Start
echo.
set /p Var="%Language_Line11%"
echo.
set /p Disk="%Language_Line12%"
echo.
echo %Language_choose1%
echo %Language_choose2%
echo %Language_choose9%
echo.
set /p Selected="%Language_Action%"
goto Select%Selected%


################
#   語言模組
################
:tw
set Language_Line11= 版本編號 ? 
set Language_Line12= 解壓縮軟體7z 所安裝的根目錄 (C or D or E ...)
set Language_choose1= 》1. 完整包       [L1J-TW_ver.XXX_FP.7z]
set Language_choose2= 》2. Src+DB標準包 [L1J-TW_ver.XXX_NP.7z]
set Language_choose9= 》9. 離開
set Language_Action=選擇：
goto Start

:cn
set Language_Line11= 版本�C�A ? 
set Language_Line12= 解���D�動^7z 所安�E的根目�� (C or D or E ...)
set Language_choose1= 》1. 完整包       [L1J-TW_ver.XXX_FP.7z]
set Language_choose2= 》2. �嵾膆]       [L1J-TW_ver.XXX_NP.7z]
set Language_choose9= 》9. 离�{
set Language_Action=�u�寣G
goto Start

:en
set Language_Line11= Version is ? 
set Language_Line12= Which Disk did 7z install for (C or D or E ...)
set Language_choose1= 》1. FullPack     [L1J-TW_ver.XXX_FP.7z]
set Language_choose2= 》2. normalPack   [L1J-TW_ver.XXX_NP.7z]
set Language_choose9= 》9. Exit
set Language_Action=Actions：
goto Start

:Select1
@%Disk%:\"Program Files"\7-Zip\7z.exe a -tzip ..\..\..\L1J-TW_ver.%Var%_FP.7z ..\..\* -r -x@Fullpack\Exclusion.lst -mx=9
goto exit

:Select2
@%Disk%:\"Program Files"\7-Zip\7z.exe a -tzip ..\..\..\L1J-TW_ver.%Var%_NP.7z -r @normalpack\Pack.lst -x@normalpack\Exclusion.lst -mx=9
goto exit

:exit
cls
exit
