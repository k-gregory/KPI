export function trapetion_one(f: (number)=> number, low: number, high: number): number {
  return (f(low) + f(high)) / 2 * (high - low);
}

export function trapetion_simple(f: (number) => number, low: number, high: number, h: number): number {
  let n = Math.ceil((high - low) / h);

  h = (high - low) / n;

  let res = 0;

  res += (f(low) + f(high)) / 2;

  for(let i = 1; i < n; i++){
    console.log(low + i * h);
    console.log(high);
    res += f(low + i * h);
  }

  res = res * h;

  return res;
}

export interface RombergResult {
  table: number[][];
  result: number,
  accuracy: number
}

export function romberg(f: (number)=>number, low : number, high: number, e : number): RombergResult {
  if(low > high) throw new Error("Low bigger than high");
  if(e <= 0)throw new Error("Too small e");

  if(low == high) return {
    table: [],
    result: 0,
    accuracy: 0
  };


  let h = high - low;
  let i = 1;
  let accuracy = NaN;

  const I : number[][] = [];
  I.push([trapetion_one(f, low, high)]);


  while (true){
    let breaked = false;
    h = h / 2;

    I.push([trapetion_simple(f, low, high, h)]);

    for(let k = 1; k <= i; k++){
      let r = (I[i][k-1] - I[i-1][k-1]) / (Math.pow(2, 2 * k) - 1);
      const new_value = I[i][k-1] + r;
      I[i].push(new_value);

      if(Math.abs(r) < e) {
        accuracy = Math.abs(r);
        breaked = true;
        break;
      }
    }
    i++;

    if(breaked){
      break;
    }

    if(i > 30) {
      throw new Error(`Can't get accuracy in ${i} iterations`);
    }
  }

  const lastA = I[I.length - 1];

  return {
    result:lastA[lastA.length - 1],
    table:  I,
    accuracy
  };
}
