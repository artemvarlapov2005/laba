\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{mietlab}[2026/10/09 Unified MIET laboratory report template]
\RequirePackage{iftex}
\ifXeTeX\XeTeXgenerateactualtext=1\fi
\ifXeTeX\else
  \ClassError{mietlab}{Compile this template with XeLaTeX}
    {Select XeLaTeX in your editor or in Overleaf Settings.}
\fi
\LoadClass[a4paper,12pt]{article}
\RequirePackage{fix-cm}
\RequirePackage[a4paper,left=30mm,right=15mm,top=20mm,bottom=20mm,
  headheight=0pt,headsep=0pt,footskip=10mm]{geometry}
\input{lab-components.tex}
\RequirePackage{tikz}
\RequirePackage{keyval}

% Реквизиты меняются одной командой \LabSetup в main.tex.
\makeatletter
\def\lab@ministry{Министерство науки и высшего образования Российской Федерации}
\def\lab@institution{Федеральное государственное автономное образовательное}
\def\lab@institutionsecond{учреждение высшего образования}
\def\lab@university{Национальный исследовательский университет «МИЭТ»}
\def\lab@institute{Институт Системной и программной инженерии и информационных технологий}
\def\lab@discipline{Основы программной инженерии}
\def\lab@number{1}
\def\lab@title{«Тест-дизайн. Техники тест-дизайна»}
\def\lab@variant{}
\def\lab@student{Варлапов Артем}
\def\lab@group{П-42}
\def\lab@studentline{Студент \lab@group}
\def\lab@city{Москва}
\def\lab@year{2026}
\define@key{lab}{ministry}{\def\lab@ministry{#1}}
\define@key{lab}{institution}{\def\lab@institution{#1}}
\define@key{lab}{institutionsecond}{\def\lab@institutionsecond{#1}}
\define@key{lab}{university}{\def\lab@university{#1}}
\define@key{lab}{institute}{\def\lab@institute{#1}}
\define@key{lab}{discipline}{\def\lab@discipline{#1}}
\define@key{lab}{number}{\def\lab@number{#1}}
\define@key{lab}{title}{\def\lab@title{#1}}
\define@key{lab}{variant}{\def\lab@variant{#1}}
\define@key{lab}{student}{\def\lab@student{#1}}
\define@key{lab}{group}{\def\lab@group{#1}}
\define@key{lab}{studentline}{\def\lab@studentline{#1}}
\define@key{lab}{city}{\def\lab@city{#1}}
\define@key{lab}{year}{\def\lab@year{#1}}
\newcommand\LabSetup[1]{\setkeys{lab}{#1}}

% Номер страницы геометрически по центру листа, а не текстового поля.
\def\ps@lab{%
  \def\@oddhead{}\def\@evenhead{}%
  \def\@oddfoot{%
    \makebox[\textwidth][l]{%
      \hspace*{\dimexpr.5\paperwidth-30mm\relax}%
      \makebox[0pt][c]{\normalfont\normalsize\thepage}}}%
  \let\@evenfoot\@oddfoot}
\pagestyle{lab}

% Базовые линии и координаты измерены в ПЕРВОМ пользовательском PDF.
% Внутри строк/переносов — 13 bp и 19.5 bp; интервалы между реквизитами
% задаются их положением на странице и не зависят от длины основного текста.
\newcommand\lab@centerline[3]{%
  \node[anchor=base,inner sep=0pt,outer sep=0pt]
    at ([xshift=318.835bp,yshift=-#1bp]current page.north west)
    {\normalfont\normalsize#2\parbox[t]{467.75bp}{\centering#3\par}};}
\newcommand\lab@rightline[3]{%
  \node[anchor=base east,inner sep=0pt,outer sep=0pt]
    at ([xshift=#2bp,yshift=-#1bp]current page.north west)
    {\normalfont\normalsize#3};}
\newcommand\MakeLabTitle{%
  \clearpage\thispagestyle{empty}%
  \hypersetup{pdftitle={Отчет по лабораторной работе №\lab@number},
    pdfauthor={\lab@student},pdfsubject={\lab@title}}%
  \null
  \begin{tikzpicture}[remember picture,overlay]
    \lab@centerline{68.88}{}{\lab@ministry}
    \lab@centerline{91.44}{}{\lab@institution}
    \lab@centerline{113.76}{}{\lab@institutionsecond}
    \lab@centerline{136.32}{}{\lab@university}
    \lab@centerline{180.96}{}{\lab@institute}
    \lab@centerline{253.92}{\bfseries}{Дисциплина: \lab@discipline}
    \lab@centerline{363.12}{\bfseries}{Отчет по лабораторной работе №\lab@number}
    \lab@centerline{399.60}{\bfseries}{\lab@title}
    \ifx\lab@variant\@empty\else
      \lab@centerline{436.08}{}{\lab@variant}
    \fi
    \lab@rightline{508.80}{552.71}{Выполнил:}
    \lab@rightline{545.28}{552.71}{\lab@studentline}
    % В исходнике ФИО вписано отдельно на 3.18 bp правее блока «Выполнил».
    \lab@rightline{582.60}{555.89}{\lab@student}
    \lab@centerline{763.92}{}{\lab@city, \lab@year}
  \end{tikzpicture}%
  \clearpage
}
\makeatother
\AtBeginDocument{\normalfont\normalsize}
