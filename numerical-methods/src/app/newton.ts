export class FirstNewton {
  _values: { x: number, y: number }[];

  constructor(_values: { x: number, y: number }[]) {
    this._values = [..._values];
    this._values.sort((a,b)=>(b.x - a.x))
  }

  _cache: { [key: string]: number } = {};

  private CalculateBase(point: number): number {
    let baseValue = Number.MAX_VALUE;
    let baseIndex = -1;
    for (let i = 0; i < this._values.length; i++) {
      const value = this._values[i].x;
      if (Math.abs(point - value) < Math.abs(point - baseValue)) {
        baseValue = value;
        baseIndex = i;
      }
    }

    return baseIndex;
  }


  private PopulateCache(baseIndex: number): number {
    const d = 100;
    const epsilon = 5 * Math.pow(10, -d);

    for (let length = 1; length + baseIndex < this._values.length; length++) {
      let isLargerThanEpsilon = false;

      for (let from = baseIndex; from + length < this._values.length; from++) {
        let delta = this.CalculateDelta(from, length);
        this._cache[FirstNewton.GetCacheKey(from, length)] = delta;
        if (Math.abs(delta) > epsilon)
          isLargerThanEpsilon = true;
      }

      if (!isLargerThanEpsilon)
        return length;
    }

    return this._values.length - baseIndex;
  }

  private CalculateDelta(from: number, length: number): number {
    if (length == 1)
      return this._values[from].y;

    return (this.CachedDelta(from + 1, length - 1) - this.CachedDelta(from, length - 1))
      / (this._values[from + length - 1].x - this._values[from].x);
  }

  private static GetCacheKey(from: number, length: number): string {
    return `${from}: ${length}`;
  }

  private CachedDelta(from: number, length: number): number {
    return this._cache[FirstNewton.GetCacheKey(from, length)];
  }

  private static Factorial(i: number): number {
    let result = 1;
    for (; i != 1; i--)
      result = result * i;
    return result;
  }

  public Interpolate(point: number) : number {
    const baseIndex = this.CalculateBase(point);
    const stoppedAt = this.PopulateCache(baseIndex);

    let acc = this._values[baseIndex].y;
    for(let length = 2; length < stoppedAt; length++){
      let xAcc = 1;
      for(let cur = 0; cur < length; cur++){
        xAcc *= (point - this._values[cur + baseIndex].x)
      }
      acc += this.CachedDelta(baseIndex, length) * xAcc / FirstNewton.Factorial(length);
    }

    return acc;
  }
}

export class SecondNewtonEvenly {
  _values: { x: number, y: number }[];

  constructor(_values: { x: number, y: number }[]) {
    this._values = [..._values];
    this._values.sort((a,b)=>(b.x - a.x))
  }

  _cache: { [key: string]: number } = {};

  private CalculateBase(point: number): number {
    let baseValue = Number.MAX_VALUE;
    let baseIndex = -1;
    for (let i = 0; i < this._values.length; i++) {
      const value = this._values[i].x;
      if (Math.abs(point - value) < Math.abs(point - baseValue)) {
        baseValue = value;
        baseIndex = i;
      }
    }

    return baseIndex;
  }


  private PopulateCache(baseIndex: number): number {
    const d = 100;
    const epsilon = 5 * Math.pow(10, -d);

    for (let length = 1; length + baseIndex < this._values.length; length++) {
      let isLargerThanEpsilon = false;

      for (let from = baseIndex; from + length < this._values.length; from++) {
        let delta = this.CalculateDelta(from, length);
        this._cache[SecondNewtonEvenly.GetCacheKey(from, length)] = delta;
        if (Math.abs(delta) > epsilon)
          isLargerThanEpsilon = true;
      }

      if (!isLargerThanEpsilon)
        return length;
    }

    return this._values.length - baseIndex;
  }

  private CalculateDelta(from: number, length: number): number {
    if (length == 1)
      return this._values[from].y;

    return (this.CachedDelta(from + 1, length - 1) - this.CachedDelta(from, length - 1));
  }

  private static GetCacheKey(from: number, length: number): string {
    return `${from}: ${length}`;
  }

  private CachedDelta(from: number, length: number): number {
    return this._cache[SecondNewtonEvenly.GetCacheKey(from, length)];
  }

  private static Factorial(i: number): number {
    let result = 1;
    for (; i != 1; i--)
      result = result * i;
    return result;
  }

  public Interpolate(point: number) : number {
    const baseIndex = this.CalculateBase(point);
    const stoppedAt = this.PopulateCache(baseIndex);

    let acc = this._values[baseIndex].y;
    for(let length = 2; length < stoppedAt; length++){
      let xAcc = 1;
      for(let cur = 0; cur < length; cur++){
        xAcc *= (point - this._values[cur + baseIndex].x)
      }
      acc += this.CachedDelta(baseIndex, length) * xAcc / SecondNewtonEvenly.Factorial(length);
    }

    return acc;
  }
}

function fact(i: number): number {
  let result = 1;
  for (; i != 1; i--)
    result = result * i;
  return result;
}

export function newton_second_evenly(f: { x: number, y: number }[], x: number) {
  f = [...f];
  f.sort((a,b)=>(b.x - a.x));

  const n = f.length;
  const columns = [f.map(e=>e.y)];
  let brkN = n;

  for(let column = 1; column < n; column++){
    const col = [];
    columns.push(col);
    for(let row = 0; row < n - column; row++){
      col.push(columns[column - 1][row + 1] - columns[column - 1][row]);
    }
    if(Math.abs(Math.min.apply(null, col)) < 1e-9){
      brkN = column;
      break;
    }
  }

  let h = (f[1].x - f[0].x);
  let q = (x - f[0].x) / h;
  let qmult = 1;
  let nMult = 1;
  let sum = f[0].y;

  for(let i = 1; i  < brkN; i++){
    qmult *= (q - i + 1);
    nMult *= (i);

    sum += qmult / nMult * columns[i][0];
  }

  return sum;
}

export function newton_first(f: {x: number, y: number}[], x: number){
  return new FirstNewton(f).Interpolate(x);
}
