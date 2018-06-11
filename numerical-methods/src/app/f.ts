export function f(x: number): number {
  const sqrt = Math.sqrt(1 - x);
  return Math.sinh(sqrt) / sqrt;
}

export const l = -13.0;
export const h = -5.0;


/*
export function f(x:number):number {
  return x * Math.cos(x/3) - Math.log(x);
}

export const l = 1;
export const h = 5;
*/
