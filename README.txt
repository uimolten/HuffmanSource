

/*  Student information for assignment:
 *
 *  On OUR honor, Ethan W and Rionn T, this programming assignment is OUR own work
 *  and WE have not provided this code to any other student.
 *
 *  Number of slip days used:
 *
 *  Student 1 (Student whose Canvas account is being used)
 *  UTEID:rrt873
 *  email address: rrt873@my.utexas.edu
 *  Grader name: 
 *
 *  Student 2
 *  UTEID: emw3442
 *  email address: emw3442@my.utexas,edu
 *
 */


Experiment Analysis:


The calgary collection (text files) compress much more compared to Waterloo (images). Calgary had
about 43% compression, while Waterloo only had about 18%.
This is probably is because Huffman coding works best when some byte values occur much more
frequently than others, because those common values receive shorter codes.

The waterloo images compressed less, likely because those files' byte frequencies were more evenly
distributed, allowing less opportunity to save space by assigning different, shorter codes to common
 values.

In general, Store tree format produces smaller compressed files than store coutn for both calgary
and waterloo when ignoring hf files. This is probably because storing the tree required less header
space, than the frequency counts.

The compression times were pretty similar when ignoring hf files. Store tree was slightly faster for
 calgary and slower for waterloo, and results do not exactly prove one or the other is consitently
 faster.

 However, including the hf files increased compression time, and the overall percent compression
 decreased, suggesting that the already compressed files had fewer opprtunities to compress further.

Finally, for BooksAndHTML, there was great compression, but the results cannot be used to compare
the header formats as the original input sizes were different between the store count and store tree
 runs.









Calgary times (SCF): 

Reading hf files
total bytes read: 5097064
total compressed bytes 3655370
total percent compression 28.285
compression time: 9.731

Ignoring hf files
total bytes read: 3251493
total compressed bytes 1845571
total percent compression 43.239
compression time: 5.030


Calgary (STF):
Ignoring hf files:
total bytes read: 3251493
total compressed bytes 1830237
total percent compression 43.711
compression time: 4.925

Reading hf files:
total bytes read: 6866341
total compressed bytes 5405137
total percent compression 21.281
compression time: 14.206




Waterloo (SCF) :
Ignoring hf files:
total bytes read: 12466304
total compressed bytes 10205282
total percent compression 18.137
compression time: 20.024


Reading hf files:
total bytes read: 22671586
total compressed bytes 20268639
total percent compression 10.599
compression time: 48.018


Waterloo (STF):
Ignoring hf files:
total bytes read: 12466304
total compressed bytes 10199808
total percent compression 18.181
compression time: 21.389

Reading hf files;
total bytes read: 38665706
total compressed bytes 36249209
total percent compression 6.250
compression time: 79.024


BOOKSANDHTML (SCF)
Ignoring hf files:
total bytes read: 9890302
total compressed bytes 5915237
total percent compression 40.192
compression time: 25.314

Reading hf files:
total bytes read: 15805539
total compressed bytes 11743264
total percent compression 25.702
compression time: 61.639

Booksandhtml (STF)
Ignoring hf files:
total bytes read: 817279
total compressed bytes 461102
total percent compression 43.581
compression time: 2.571

Reading hf files:
total bytes read: 1278381
total compressed bytes 915773
total percent compression 28.365
compression time: 5.491



