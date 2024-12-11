import {Item} from "./item";


export class Checklist {
  id: string;
  ueberschrift: string;
  abteilungsname: string;
  position: string;
  items: Item[];

  constructor(
    id: string,
    ueberschrift: string,
    abteilungsname: string,
    position: string,
    items: Item[]
  ) {
    this.id = id;
    this.ueberschrift = ueberschrift;
    this.abteilungsname = abteilungsname;
    this.position = position;
    this.items = items;
  }
}
