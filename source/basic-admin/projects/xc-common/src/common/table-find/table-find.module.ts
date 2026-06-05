import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TableFindComponent} from './table-find.component';
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzBreadCrumbModule} from 'ng-zorro-antd/breadcrumb';
import {NzRadioModule} from 'ng-zorro-antd/radio';
import {NzProgressModule} from 'ng-zorro-antd/progress';
import {PipeModule} from '../pipe/pipe.module';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzTreeSelectModule} from 'ng-zorro-antd';
import {FormInfoModule} from '../form-info/form-info.module';
import {MultiSelectQueryModule} from '../multi-select-query/multi-select-query.module';

@NgModule({
  declarations: [TableFindComponent],
  exports: [TableFindComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzTableModule,
    NzButtonModule,
    NzIconModule,
    NzInputModule,
    NzBreadCrumbModule,
    NzRadioModule,
    NzProgressModule,
    PipeModule,
    NzSelectModule,
    NzTreeSelectModule,
    FormInfoModule,
    MultiSelectQueryModule,
  ]
})
export class TableFindModule {
}
