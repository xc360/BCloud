import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TableDetailComponent} from './table-detail.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NzButtonModule} from 'ng-zorro-antd';
import {PipeModule} from '../pipe/pipe.module';

@NgModule({
  declarations: [TableDetailComponent],
  exports: [TableDetailComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzButtonModule,
    ReactiveFormsModule,
    PipeModule
  ]
})
export class TableDetailModule {
}
