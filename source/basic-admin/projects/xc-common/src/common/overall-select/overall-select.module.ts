import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OverallSelectComponent} from './overall-select.component';
import {NzFormModule, NzSelectModule} from 'ng-zorro-antd';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [OverallSelectComponent],
  exports: [
    OverallSelectComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    NzFormModule,
    NzSelectModule
  ]
})
export class OverallSelectModule {
}
