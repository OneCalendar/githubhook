package model

import org.codehaus.jackson.annotate.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
case class PayloadRepository(url:String)

@JsonIgnoreProperties(ignoreUnknown = true)
case class PayloadAuthor(email:String)

@JsonIgnoreProperties(ignoreUnknown = true)
case class PayloadCommit(author:PayloadAuthor)

@JsonIgnoreProperties(ignoreUnknown = true)
case class Payload (ref:String,repository:PayloadRepository,commits:Seq[PayloadCommit])
