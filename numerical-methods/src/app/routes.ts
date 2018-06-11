import {Routes} from "@angular/router";
import {TrapezoidComponent} from "./trapezoid/trapezoid.component";
import {NewtonSecondEvenlyComponent} from "./newton-second-evenly/newton-second-evenly.component";
import {NewtonFirstComponent} from "./newton-first/newton-first.component";
import {RombergComponent} from "./romberg/romberg.component";


export const routes: Routes = [{
  path: 'interpolation/newton-second-evenly',
  component: NewtonSecondEvenlyComponent,
  data: {
    name: 'Другий метод Ньютона з рівновіддаленими вузлами'
  }
}, {
  path: 'interpolation/newton-first',
  component: NewtonFirstComponent,
  data: {
    name: 'Перший метод Ньютона з нерівновіддаленими вузлами'
  }
}, {
  path: 'integration/trapezoid',
  component: TrapezoidComponent,
  data: {
    name: 'Складена квадратурна формула трапецій'
  }
}, {
  path: 'integration/romberg',
  component: RombergComponent,
  data: {
    name: 'Алгоритм Ромберга'
  }
}];
