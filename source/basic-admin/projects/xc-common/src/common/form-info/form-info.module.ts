import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormInfoComponent} from './form-info.component';
import {FormsModule} from '@angular/forms';
import {NzButtonModule, NzFormModule, NzIconModule, NzInputModule, NzSelectModule, NzSwitchModule} from 'ng-zorro-antd';
import {PipeModule} from '../pipe/pipe.module';
import {NzInputNumberModule} from 'ng-zorro-antd/input-number';
import {NzTreeSelectModule} from 'ng-zorro-antd/tree-select';
import {NzDatePickerModule} from 'ng-zorro-antd/date-picker';
import {LinkageSelectModule} from '../linkage-select/linkage-select.module';
import {NzTimePickerModule} from 'ng-zorro-antd/time-picker';

@NgModule({
  declarations: [FormInfoComponent],
  exports: [FormInfoComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzFormModule,
    NzInputModule,
    NzSelectModule,
    NzSwitchModule,
    PipeModule,
    NzInputNumberModule,
    NzTreeSelectModule,
    NzDatePickerModule,
    NzIconModule,
    NzButtonModule,
    LinkageSelectModule,
    NzTimePickerModule
  ]
})
export class FormInfoModule {
}
