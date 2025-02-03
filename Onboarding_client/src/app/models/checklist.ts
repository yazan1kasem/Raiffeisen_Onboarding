import {Item} from "./item";


export class Checklist {
  id: string;
  abteilungsname: string;
  position: string;
  items: Item[];

  constructor(
    id: string,
    abteilungsname: string,
    position: string,
    items: Item[]
  ) {
    this.id = id;
    this.abteilungsname = abteilungsname;
    this.position = position;
    this.items = items;
  }
}
