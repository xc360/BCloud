import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MultiSelectQueryComponent} from './multi-select-query.component';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [MultiSelectQueryComponent],
  exports: [MultiSelectQueryComponent],
  imports: [
    CommonModule,
    FormsModule
  ]
})
export class MultiSelectQueryModule {
}
