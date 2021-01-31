case class Activities(act: String*) {
  override def toString = act.toList.fold("")((a, b) => s"$a\n$b")
}



