import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ListFindComponent} from './list-find.component';
import {FormsModule} from '@angular/forms';
import {
  NzBreadCrumbModule,
  NzButtonModule,
  NzIconModule,
  NzInputModule, NzListModule, NzPaginationModule, NzProgressModule,
  NzRadioModule
} from 'ng-zorro-antd';
import {PipeModule} from '../pipe/pipe.module';
import {NzDropDownModule} from 'ng-zorro-antd/dropdown';
import {NzCheckboxModule} from 'ng-zorro-antd/checkbox';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';

@NgModule({
  declarations: [ListFindComponent],
  exports: [ListFindComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzButtonModule,
    NzIconModule,
    NzInputModule,
    NzBreadCrumbModule,
    NzRadioModule,
    NzProgressModule,
    PipeModule,
    NzListModule,
    NzPaginationModule,
    NzDropDownModule,
    NzCheckboxModule,
    NzToolTipModule
  ]
})
export class ListFindModule {
}
