package models

case class MatrixLine(line: String, transpose: Boolean, cellNames: Boolean, geneNames: Boolean)
case class MatrixParams(minMarkers: Int, maxMarkers: Int)