//var minGene = document.getElementById("myRange1");
var maxGene= document.getElementById("myRange2");


function mOver(obj) {
    /*var allSquares = document.getElementsByClassName("square")
    //console.log(allSquares)
    for (let square of allSquares){
        //square.innerHTML = "MM"
        //square.style.transform = "translateY(10px)"
    }
    //console.log(obj.id[1])
    //obj.innerHTML = "Thank you"*/

    obj.style.transform = "scaleY(1.5) scaleX(1.5)"
    //obj.style.position = "relative"
    obj.style.zIndex = "1000"
    //console.log(parseInt(obj.style.width))
    obj.style.width = parseInt(obj.style.width)-4 + "px"
    obj.style.height = parseInt(obj.style.height)-4 + "px"
    obj.style.borderStyle = "solid"
    obj.style.borderColor = "white"
    obj.style.borderWidth = "2px"
    //obj.style.transform = "scaleX(1.5)"
}

function mOut(obj) {
    //obj.innerHTML = "Thank you"
    obj.style.width = parseInt(obj.style.width)+4 + "px"
    obj.style.height = parseInt(obj.style.height)+4 + "px"
    obj.style.transform = "scaleY(1)"
    obj.style.transform = "scaleX(1)"
    obj.style.zIndex = "0"
    obj.style.borderStyle = "none"
    //var square = document.getElementById("11")
    //console.log("do nothing")
}

/**
 *
 * Currently useless but already gets the form values
 */
function submit(){

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:9000/line", true)
    var line = '{\"line\": \"1,2,3,4,5,6,7,8,9\"}'
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function() { //Appelle une fonction au changement d'état.
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
          console.log("Done request")
          // Requête finie, traitement ici.
        }
    }

    xhr.send(line)
}


function getter(){
    const url = "http://localhost:9000/lines"
    //.then(res => console.log(res.body.json()))
    fetch(url)
    .then(data=>{return data.json()})
    .then(res=>{console.log(res)})

}

function getbicluster(){
    const url = "http://localhost:9000/getbicluster"
    //.then(res => console.log(res.body.json()))
    fetch(url)
    .then(data=>{return data.json()})
    .then(res=>{repBicluster(res)})

}


function preprocess(){
    const url = "http://localhost:9000/preprocess"
    //.then(res => console.log(res.body.json()))
    fetch(url)
    .then(data=>{return data.json()})
    .then(res=>{console.log(res)})
}


function matrixGetter(){
    const url = "http://localhost:9000/build"
    //.then(res => console.log(res.body.json()))
    fetch(url)
    .then(data=>{return data.json()})
    .then(res=>{console.log(res)})

}

function normalize(){
    const url = "http://localhost:9000/normalize"
    //.then(res => console.log(res.body.json()))
    fetch(url)
    .then(data=>{return data.json()})
    .then(res=>{console.log(res)})
}

function sendParams(){
  var minGene = document.getElementById("myRange1");
  var maxGene = document.getElementById("myRange2");
  var minGeneValue = minGene.value
  var maxGeneValue = maxGene.value
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "http://localhost:9000/params", true)
  var line = '\{\"minMarkers\":'+ minGeneValue + ', \"maxMarkers\":' + maxGeneValue + '}'
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function() { //Appelle une fonction au changement d'état.
      if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
        console.log("Done request")
        // Requête finie, traitement ici.
      }
  }

  xhr.send(line)
}

function reset(){
  window.location.reload()
  const url = "http://localhost:9000/reset"
  //.then(res => console.log(res.body.json()))
  fetch(url)
  .then(data=>{return data.json()})
  .then(res=>{console.log(res)})

}

function getParams(){
  const url = "http://localhost:9000/sendparams"
  //.then(res => console.log(res.body.json()))
  fetch(url)
  .then(data=>{return data.json()})
  .then(res=>{console.log(res)})
}

function solve(){
    const url = "http://localhost:9000/solve"
    //.then(res => console.log(res.body.json()))
    fetch(url)
    .then(data=>{return data.json()})
    .then(res=>{console.log(res)})
}


function getResults(){
    const url = "http://localhost:9000/sendresults"
    //.then(res => console.log(res.body.json()))
    fetch(url)
    .then(data=>{return data.json()})
    .then(res=>{console.log(res)})
}

function repBicluster(biclusterAsCSV){
    var map = document.getElementById("fleetContent")
    var z = document.createElement('div')
    lines = biclusterAsCSV.split('\n').map(function (line){
        var splitLine = line.split(',')
        var numberLine = []
        //console.log(splitLine)
        for(let str of splitLine){
            numberLine.push(parseInt(str))
        }
        return numberLine
    })
    z.innerHTML = buildHTMLString(lines.length, lines[0].length, lines)
    map.append(z)

}

function represent(){
  console.log("inside editpage")
  var map = document.getElementById("fleetContent")
  var fileSelector = document.getElementById("file-selector").files[0]
  //console.log(fileSelector)
  //map.innerHTML = ""
  const reader = new FileReader()
  var lines = []

  reader.onload = function(){
      lines = reader.result.split('\n').map(function (line){

          var splitLine = line.split(',')
          var numberLine = []
          //console.log(splitLine)
          for(let str of splitLine){
              numberLine.push(parseInt(str))
          }
          return numberLine
      })
      //console.log(lines)
      map.innerHTML = buildHTMLString(lines.length, lines[0].length, lines)
  }
  reader.readAsText(fileSelector)
}


function editPage(){
    //console.log("inside editpage")
    var map = document.getElementById("fleetContent")
    var fileSelector = document.getElementById("file-selector").files[0]
    //console.log(fileSelector)
    //map.innerHTML = ""
    const reader = new FileReader()
    var lines = []
    var transpose = document.getElementById('transpose').checked
    var geneNames = document.getElementById('geneNames').checked
    var cellNames = document.getElementById('cellNames').checked

    console.log(transpose)
    console.log(geneNames)
    console.log(cellNames)

    reader.onload = function(){
        lines = reader.result
        //console.log(lines)
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:9000/line", true)
        xhr.setRequestHeader("Content-Type", "application/json");
        var l = {"line": lines, "transpose": transpose, "cellNames": cellNames, "geneNames": geneNames}
        //console.log(l)
        xhr.onreadystatechange = function() { //Appelle une fonction au changement d'état.
            if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
              console.log("Done request")
            // Requête finie, traitement ici.
            }
        }

        xhr.send(JSON.stringify(l))
    }

    try{
      reader.readAsText(fileSelector)
    }catch(error){
      alert('There was a problem while reading the file')
    }

}

function editPageReq(){
    console.log("inside editpage")
    var map = document.getElementById("fleetContent")
    var fileSelector = document.getElementById("file-selector").files[0]
    console.log(fileSelector)
    map.innerHTML = ""
    const reader = new FileReader()
    var lines = []

    /*const files = document.getElementById("file-selector")
    console.log(files)*/
    const formData = new FormData()
    formData.append('data', fileSelector)

    const xhr = new XMLHttpRequest()
    xhr.onload = () => {
        console.log(xhr.responseText)
    }

    xhr.open("POST", "http://localhost:3000/compute", true)
    xhr.send(formData)

}

/**
 *
 *
 */
function buildHTMLString(nLine, nCol, valueArray){
    //square example
    //<div class="square" id="11" onmouseover="mOver(this)" onmouseout="mOut(this)">
    //Mouse Over Me
    //</div>
    //height and width should be editted
    /**
     * FOXP3
CCDC22
DUSP4
ENTPD1
TIGIT
ENTPD1−AS1
HLA−DPA1
F5
TNFRSF4
TNFRSF18
DUSP16
AP001610.1
CCR8
ICOS
LAIR2
CTLA4
DUSP10
HLA−DRB1
IL2RA
AL137186.1
DNPH1
HLA−DRB5
GBP2
TNFRSF1B
GLRX
     */
    var geneNames = ["ASF1B","TYMS","UBE2T","TCF19","TK1","RAD51AP1","CLSPN","ATAD5","KIF11","ZWINT", "UHRF1", "RAD54L", "PCLAF","RRM2","FAM111B", "CDK1", "CDCA5", "PKMYT1", "CDCA5", "EXO1", "BIRC5", "NCAPG", "MAD2L1", "TYMSOS", "CDCA7", "DTL"]
    var squareWidth = parseInt((screen.availWidth-400)/(nCol+2))
    var squareHeight = parseInt((screen.availHeight-200)/(nLine+2))
    var squareSize = squareWidth//Math.min(squareHeight,squareWidth)
    console.log("height",squareHeight)
    console.log("width",squareWidth)
    var htmlString = []
    //var counter =
    console.log(nLine)
    console.log(nCol)
    for (var i = 0 ; i < nCol ; i++){
        //console.log(i)
        var openDiv = "<div class=\"col\" id=\"" + i + "\">"
        htmlString.push(openDiv)
        //str= "<div class=\"square\" style=\"border-radius: 0; width:"+ squareSize +"px;height:"+ squareSize +"px;\">"+ (i+1) +"</div>"
        //htmlString.push(str)
        for(var j = 0 ; j < nLine-1 ; j++){
            /*<div class="square" onmouseover="mOver(this)" onmouseout="mOut(this)" style="background-color:#D94A38;width:120px;height:20px;padding:40px;"></div>*/
            str= "<div class=\"square\" onmouseover=\"mOver(this)\" onmouseout=\"mOut(this)\" style=\"background-color:"+ colorBuilder(valueArray[j][i]) +";width:"+ squareSize +"px;height:"+ squareSize +"px;\"></div>"
            htmlString.push(str)
            //console.log(id)
        }
        htmlString.push("</div>")
    }
    var openDiv = "<div class=\"col\" id=\"" + i + "\">"
    if(i == 168){
      console.log()
    }
    htmlString.push(openDiv)
    //str= "<div class=\"square\" style=\"border-radius: 0; width:"+ squareSize +"px;height:"+ (squareSize+(parseInt(squareSize/3))) +"px;\"></div>"
    for(var j =0 ; j < nLine; j++){
        if(typeof geneNames[j] !== 'undefined' ){
            str= "<div class=\"squareText\" style=\"width:"+ squareSize +"px;height:"+ squareSize +"px;\">"+ geneNames[j] +"</div>"
        }else{
            str= "<div class=\"squareText\" style=\"width:"+ squareSize +"px;height:"+ squareSize +"px;\"></div>"
        }

        htmlString.push(str)
    }
    htmlString.push("</div>")
    return htmlString.join("")

}


/**
 * TODO convert the values with a formula or find a function that does it
 * TODO compute scale based on dataset max/min value
 * @param {*} squareValue : gene expression value
 * return : a string that corresponds to an Hexadecimal color
 */
function colorBuilder(squareValue){
    // Between -1 and 0 from blue to green
    // over 0 from green to red
    //5 = 255 0  0
    //4 = 215 48 39
    //3 = 252 158 100
    //2 = 254 235 163
    //1 = 235 247 227
    //0 = 156 198 223
    //-1 = 69 117 180
    if(squareValue==5) {
        return "#FF0000"
    }else if(squareValue==4){
        return "#D73027"
    }else if(squareValue==3){
        return "#FC9E64"
    }else if(squareValue==2){
        return "#FEEBA3"
    }else if(squareValue==1){
        return "#EBF7E3"
    }else if(squareValue==0){
        return "#9CC6DF"
    }
    return "#4575B4"
}
