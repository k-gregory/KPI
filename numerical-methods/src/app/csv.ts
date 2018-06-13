import * as Papa from 'papaparse'

export function decodeCsv(csvSrc): {x: number, y: number}[] {
  const data = Papa.parse(csvSrc);
  if(data.errors.length != 0) {
    console.log(data.errors);
    throw new Error("Can't parse");
  }

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

export function getControls(csvSrc: string): number[] {
  const data = Papa.parse(csvSrc);
  if(data.errors.length != 0){
    console.log(data.errors);
    throw new Error("Can't parse")
  }

  return data.data.filter(e=>e.length != 0)[0].map(e=>{
    const r = parseFloat(e);
    if(isNaN(r)) throw new Error("Nan");
    return r;
  });
}
