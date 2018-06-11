export function trapetion_simple(f: (number) => number, low: number, high: number, h: number): number {
  let n = Math.ceil((high - low) / h);
  h = (high - low) / n;

  let res = 0;

  res += (f(low) + f(high)) / 2;

  for(let i = 1; i < n; i++){
    res += f(low + i * h);
  }

  res = res * h;

  return res;
}
