confusionmatrix-weka-package
============================

Weka package containing different visualizations of a classifier's confusion matrix in the Explorer.

Available visualizations:

* **text** -  slightly enhanced default text representation, can be saved as text file or printed

* **table** - representing the matrix in a JTable, can be saved as CSV file or printed

* **heatmap** - counts in the matrix get represented using colors chosen from a gradient generated from two colors, can be saved as image file

* **heatmap (scaled)** - same as **heatmap**, but divides the elements in a row by the sum of counts in that row (= percentages). Useful for skewed class distributions.


Releases
--------

* [2014.10.15](https://github.com/fracpete/confusionmatrix-weka-package/releases/download/v2014.10.15/confusionmatrix-2014.10.15.zip)
* [2014.8.6](https://github.com/fracpete/confusionmatrix-weka-package/releases/download/v2014.8.6/confusionmatrix-2014.8.6.zip)
* [2014.8.5](https://github.com/fracpete/confusionmatrix-weka-package/releases/download/v2014.8.5/confusionmatrix-2014.8.5.zip)


How to use packages
-------------------

For more information on how to install the package, see:

http://weka.wikispaces.com/How+do+I+use+the+package+manager%3F


Maven
-----

Add the following dependency in your `pom.xml` to include the package:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>confusionmatrix-weka-package</artifactId>
      <version>2014.10.15</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>nz.ac.waikato.cms.weka</groupId>
          <artifactId>weka-dev</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```

