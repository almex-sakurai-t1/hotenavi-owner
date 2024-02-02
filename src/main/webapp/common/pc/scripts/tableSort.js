var TableSort = {};
TableSort.tables = [];
TableSort.lastSort = [];
if (window.onload) {
  TableSort.oldOnload = window.onload;
}
window.onload = function() {
  TableSort.init();
  if (TableSort.oldOnload) {
    TableSort.oldOnload();
  }
}

TableSort.init = function() {
  if (navigator.appName == "Microsoft Internet Explorer" && navigator.platform.indexOf("Mac") == 0) {
    return;
  }
  if (arguments.length == 0) {
    var tableNodeList = document.getElementsByTagName('TABLE');
    for (var x = 0; x < tableNodeList.length; x++) {
      TableSort.tables.push(tableNodeList[x]);
      TableSort.initTable(x);
    }
  } else {
    var table;
    for (var x = 0; x < arguments.length; x++) {
      table = document.getElementById(arguments[x]);
      if (table) {
        TableSort.tables.push(table);
        TableSort.initTable(TableSort.tables.length - 1);
      }
    }
  }
}

TableSort.initTable = function(t) {
  var table = TableSort.tables[t];
  if (table.tHead) {
    for (var y = 0; y < table.tHead.rows.length; y++) {
      for (var x = 0; x < table.tHead.rows[y].cells.length; x++) {
        TableSort.linkCell(table.tHead.rows[y].cells[x], t, x, y);
      }
    }
  }
  TableSort.lastSort[t] = 0;
}


// 見出しのソートリンク箇所
TableSort.linkCell = function(cell, t, x, y) {
  if (TableSort.getLabel(cell)) {
    var link = document.createElement('A');
    link.href = "#Sort_" + t + "_" + x;
    link.style.color="#FFFFFF";
    link.style.fontWeight="normal";
    link.onclick = new Function("TableSort.click(" + t + ", " + x + ", \"" +
        escape(TableSort.getLabel(cell)) + "\"); return false");
    cell.appendChild(link);
    for (var c = 0; c < cell.childNodes.length - 1; c++) {
      link.appendChild(cell.childNodes[c]);
    }

    var arrow = document.createElement('SPAN');
    arrow.innerHTML = TableSort.arrowNone;
    arrow.name = "TableSort_" + t + "_" + x + "_" + y;
    cell.appendChild(arrow);
    link.title = link.innerHTML + "で並べ替えます";
  }
}


// ラベル（'label'）にて判断
TableSort.getLabel = function(cell) {
  var str;
  if (window.opera) {
    var m = cell.outerHTML.match(/^<[^>]+LABEL=['"]*([^'" ]+)['"]*/i);
    str = m ? m[1] : "";
  } else {
    str = cell.getAttribute('label');
  }
  return str ? str.toLowerCase() : '';
}


// Sort the rows in this table by the specified column.
TableSort.click = function(table, column, mode) {
  if (!mode.match(/^[_a-z0-9]+$/)) {
    alert("ソートモードエラー");
    return;
  }
  var compareFunction = eval("TableSort.compare_" + mode);
  if (typeof compareFunction != "function") {
    alert("ソートモードが変: " + mode);
    return;
  }

  var reverse = true;
  if (column > 1) //先頭の2列は対象外（店舗順なので常に昇順・最初に読み込んだ数字順=display:none）
  {
      if (Math.abs(TableSort.lastSort[table]) == column + 1) {
        reverse = TableSort.lastSort[table] < 0;
        TableSort.lastSort[table] = -TableSort.lastSort[table];
      } else {
        TableSort.lastSort[table] = column+1;
      }
  }
  else
  {
      reverse = false;
      TableSort.lastSort[table] = 1;
  }
  
  // Display the correct arrows on every header/footer cell.
  var spans = document.getElementsByTagName('SPAN');
  var spanprefix1 = "TableSort_" + table + "_";
  var spanprefix2 = "TableSort_" + table + "_" + column;
  for (var s = 0; s < spans.length; s++) {
    if (spans[s].name && spans[s].name.substring(0, spanprefix1.length) ==
        spanprefix1) {
      if (spans[s].name.substring(0, spanprefix2.length) == spanprefix2) {
        if (TableSort.lastSort[table] > 0) {
          spans[s].innerHTML = TableSort.arrowDown;
        } else {
          spans[s].innerHTML = TableSort.arrowUp;
        }
      } else {
        spans[s].innerHTML = TableSort.arrowNone;
      }
    }
  }

  if (TableSort.tables[table].tBodies.length < 1) {
    return; // テーブルにデータがない.
  }
  var tablebody = TableSort.tables[table].tBodies[0];
  var cellDictionary = [];
  var cell;
  for (var y = 0; y < tablebody.rows.length; y++) {
    if (tablebody.rows[y].cells.length > 0) {
      cell = tablebody.rows[y].cells[column];
    } else { // Dodge Safari 1.0.3 bug
      cell = tablebody.rows[y].childNodes[column];
    }
    cell.innerHTML = cell.innerHTML.replace(/,/g,""); //カンマをはずす
    cellDictionary[y] = [TableSort.dom2txt(cell), tablebody.rows[y]];
  }

  // ソートの実行
  cellDictionary.sort(compareFunction);

  for (var y = 0; y < tablebody.rows.length; y++) {
    if (tablebody.rows[y].cells.length > 0) {
      cell = tablebody.rows[y].cells[column];
    } else { // Dodge Safari 1.0.3 bug
      cell = tablebody.rows[y].childNodes[column];
    }
    cell.innerHTML = separate(cell.innerHTML); //カンマをもとに戻す
  }



  // 新しい順番で表示.
  var i;
  for (y = 0; y < cellDictionary.length; y++) {
    i = reverse ? (cellDictionary.length - 1 - y) : y;
    tablebody.appendChild(cellDictionary[i][1]);
  }
  if (window.opera) {
    // Opera needs to rerender the last row due to a redraw bug.
    setTimeout(function() {
      // This is a closure.
      tablebody.appendChild(tablebody.removeChild(
          tablebody.rows[tablebody.rows.length - 1]));
    }, 1);
  }

  //並べ替えた後に背景色を変更する
  for (y = 0; y < tablebody.rows.length; y++) {
    backColor = "#FFFFFF";
    if (y % 2 == 0)
    {
       backColor = "#CCCCCC";
    }
    for (x = 0; x < tablebody.rows[y].cells.length; x++) {
      tablebody.rows[y].cells[x].style.backgroundColor=backColor;
    }
  }
}

function separate(num){
    return String(num).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
}

TableSort.dom2txt = function(obj) {
  var text = "";
  if (!obj) {
    return "";
  }
  if (obj.nodeType == 3) {
    text = obj.data;
  } else {
    for (var x = 0; x < obj.childNodes.length; x++) {
      text += TableSort.dom2txt(obj.childNodes[x]);
    }
  }
  return text;
}


TableSort.compare_case = function(a, b) {
  if (a[0] == b[0]) {
    return 0;
  }
  return (a[0] > b[0]) ? 1 : -1;
}


TableSort.compare_nocase = function(a, b) {
  var aLower = a[0].toLowerCase();
  var bLower = b[0].toLowerCase();
  if (aLower == bLower) {
    return 0;
  }
  return (aLower > bLower) ? 1 : -1;
}

TableSort.compare_num = function(a, b) {
  var aNum = parseFloat(a[0]);
  if (isNaN(aNum)) {
    aNum = -Number.MAX_VALUE;
  }
  var bNum = parseFloat(b[0]);
  if (isNaN(bNum)) {
    bNum = -Number.MAX_VALUE;
  }
  if (aNum == bNum) {
    return 0;
  }
  return (aNum > bNum) ? 1 : -1;
}
TableSort.arrowNone = " <img height=16 width=8 src='../../common/pc/image/sort_done.png' alt=''>";
TableSort.arrowUp = " <img height=16 width=8 src='../../common/pc/image/sort_asc.png' alt='↑'>";
TableSort.arrowDown = " <img height=16 width=8 src='../../common/pc/image/sort_desc.png' alt='↓'>";
