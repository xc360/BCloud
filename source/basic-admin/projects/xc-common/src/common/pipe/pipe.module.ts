// angular
import {NgModule} from '@angular/core';
import {SystemI18nPipe} from './system-i18n.pipe';
import {BasicPipe} from './basic.pipe';
import {DictListPipe} from './dict-list.pipe';
import {DictDescPipe} from './dict-desc.pipe';
import {ButtonAuthorityPipe} from './button-authority.pipe';

@NgModule({
  declarations: [SystemI18nPipe, BasicPipe, DictListPipe, DictDescPipe, ButtonAuthorityPipe],
  imports: [],
  exports: [SystemI18nPipe, BasicPipe, DictListPipe, DictDescPipe, ButtonAuthorityPipe]
})
export class PipeModule {
}
