

Experiment Analysis:


The calgary collection (text files) compress much more compared to Waterloo (images). 
This probably is because Huffman coding works best when some byte values occur much more frequently
than others, because those common values receive shorter codes.

The waterloo images compressed less because those images had lots of different colors and patterns
which resulted in more distributed byte frequencies, and less opportunities for shortening their
codes.

Store Tree format tends to take longer to compress than Store Count format, more noticable on image
files and when we read hf files instead of ignoring.

When we added already compressed files, the compression time roughly doubled, but the percent compression
was mostly less than double. 









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



