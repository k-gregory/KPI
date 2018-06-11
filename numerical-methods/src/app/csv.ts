import * as Papa from 'papaparse'

export function decodeCsv(csvSrc): {x: number, y: number}[] {
  const data = Papa.parse(csvSrc);
  if(data.errors.length != 0)
    throw new Error("Can't parse");

  const arr: any[] = data.data;

  const map = arr
    .filter(c=>c.length == 2)
    .map(el=>({x: Number.parseFloat(el[0]), y: Number.parseFloat(el[1])}));

  map.forEach(el=>{
    if(isNaN(el.x) || isNaN(el.y))
      throw new Error("Not a number")
  });

  return map;
}
